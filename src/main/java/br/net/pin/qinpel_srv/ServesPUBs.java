package br.net.pin.qinpel_srv;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesPUBs extends HttpServlet {

  private static final int DEFAULT_BUFFER_SIZE = 10240; // ..bytes = 10KB.
  private static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.
  private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

  private String basePath;

  @Override
  public void init() throws ServletException {
    this.basePath = getInitParameter("basePath");
    if (this.basePath == null) {
      throw new ServletException("ServesPub init param 'basePath' is required.");
    } else {
      var path = new File(this.basePath);
      if (!path.exists()) {
        throw new ServletException("ServesPub init param 'basePath' value '"
            + this.basePath + "' does actually not exist in file system.");
      } else if (!path.isDirectory()) {
        throw new ServletException("ServesPub init param 'basePath' value '"
            + this.basePath + "' is actually not a directory in file system.");
      } else if (!path.canRead()) {
        throw new ServletException("ServesPub init param 'basePath' value '"
            + this.basePath + "' is actually not readable in file system.");
      }
    }
  }

  @Override
  protected void doHead(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response, false);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response, true);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response,
      boolean content) throws IOException {
    var requestedFile = request.getPathInfo();
    if (requestedFile == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    var file = new File(basePath, URLDecoder.decode(requestedFile, "UTF-8"));
    if (!file.exists()) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    var fileName = file.getName();
    long length = file.length();
    long lastModified = file.lastModified();
    var eTag = fileName + "_" + length + "_" + lastModified;
    long expires = System.currentTimeMillis() + DEFAULT_EXPIRE_TIME;

    var ifNoneMatch = request.getHeader("If-None-Match");
    if (ifNoneMatch != null && matches(ifNoneMatch, eTag)) {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      response.setHeader("ETag", eTag);
      response.setDateHeader("Expires", expires);
      return;
    }
    var ifModifiedSince = request.getDateHeader("If-Modified-Since");
    if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince
        + 1000 > lastModified) {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      response.setHeader("ETag", eTag);
      response.setDateHeader("Expires", expires);
      return;
    }
    var ifMatch = request.getHeader("If-Match");
    if (ifMatch != null && !matches(ifMatch, eTag)) {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
      return;
    }
    var ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
    if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
      return;
    }

    var full = new Range(0, length - 1, length);
    var ranges = new ArrayList<Range>();
    var range = request.getHeader("Range");
    if (range != null) {
      if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
        response.setHeader("Content-Range", "bytes */" + length);
        response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
        return;
      }

      var ifRange = request.getHeader("If-Range");
      if (ifRange != null && !ifRange.equals(eTag)) {
        try {
          var ifRangeTime = request.getDateHeader("If-Range");
          if (ifRangeTime != -1 && ifRangeTime + 1000 < lastModified) {
            ranges.add(full);
          }
        } catch (IllegalArgumentException ignore) {
          ranges.add(full);
        }
      }

      if (ranges.isEmpty()) {
        for (String part : range.substring(6).split(",")) {
          var start = sublong(part, 0, part.indexOf("-"));
          var end = sublong(part, part.indexOf("-") + 1, part.length());
          if (start == -1) {
            start = length - end;
            end = length - 1;
          } else if (end == -1 || end > length - 1) {
            end = length - 1;
          }
          if (start > end) {
            response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
            response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
          }
          ranges.add(new Range(start, end, length));
        }
      }
    }

    var contentType = getServletContext().getMimeType(fileName);
    var acceptsGzip = false;
    var disposition = "inline";

    if (contentType == null) {
      contentType = "application/octet-stream";
    }
    if (contentType.startsWith("text")) {
      var acceptEncoding = request.getHeader("Accept-Encoding");
      acceptsGzip = acceptEncoding != null && accepts(acceptEncoding, "gzip");
      contentType += ";charset=UTF-8";
    } else if (!contentType.startsWith("image")) {
      var accept = request.getHeader("Accept");
      disposition = accept != null && accepts(accept, contentType) ? "inline"
          : "attachment";
    }

    response.reset();
    response.setBufferSize(DEFAULT_BUFFER_SIZE);
    response.setHeader("Content-Disposition", disposition + ";filename=\"" + fileName
        + "\"");
    response.setHeader("Accept-Ranges", "bytes");
    response.setHeader("ETag", eTag);
    response.setDateHeader("Last-Modified", lastModified);
    response.setDateHeader("Expires", expires);

    RandomAccessFile input = null;
    OutputStream output = null;
    try {
      input = new RandomAccessFile(file, "r");
      output = response.getOutputStream();
      if (ranges.isEmpty() || ranges.get(0) == full) {
        var r = full;
        response.setContentType(contentType);
        if (content) {
          if (acceptsGzip) {
            response.setHeader("Content-Encoding", "gzip");
            output = new GZIPOutputStream(output, DEFAULT_BUFFER_SIZE);
          } else {
            response.setHeader("Content-Length", String.valueOf(r.length));
          }
          copy(input, output, r.start, r.length);
        }
      } else if (ranges.size() == 1) {
        var r = ranges.get(0);
        response.setContentType(contentType);
        response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/"
            + r.total);
        response.setHeader("Content-Length", String.valueOf(r.length));
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
        if (content) {
          copy(input, output, r.start, r.length);
        }
      } else {
        response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        if (content) {
          var sos = (ServletOutputStream) output;
          for (var r : ranges) {
            sos.println();
            sos.println("--" + MULTIPART_BOUNDARY);
            sos.println("Content-Type: " + contentType);
            sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);
            copy(input, output, r.start, r.length);
          }
          sos.println();
          sos.println("--" + MULTIPART_BOUNDARY + "--");
        }
      }
    } finally {
      close(output);
      close(input);
    }
  }

  private static boolean accepts(String acceptHeader, String toAccept) {
    var acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
    Arrays.sort(acceptValues);
    return Arrays.binarySearch(acceptValues, toAccept) > -1 || Arrays.binarySearch(
        acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1 || Arrays.binarySearch(
            acceptValues, "*/*") > -1;
  }

  private static boolean matches(String matchHeader, String toMatch) {
    var matchValues = matchHeader.split("\\s*,\\s*");
    Arrays.sort(matchValues);
    return Arrays.binarySearch(matchValues, toMatch) > -1 || Arrays.binarySearch(
        matchValues, "*") > -1;
  }

  private static long sublong(String value, int beginIndex, int endIndex) {
    var substring = value.substring(beginIndex, endIndex);
    return (substring.length() > 0) ? Long.parseLong(substring) : -1;
  }

  private static void copy(RandomAccessFile input, OutputStream output, long start,
      long length) throws IOException {
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int read;
    if (input.length() == length) {
      while ((read = input.read(buffer)) > 0) {
        output.write(buffer, 0, read);
      }
    } else {
      input.seek(start);
      long toRead = length;
      while ((read = input.read(buffer)) > 0) {
        if ((toRead -= read) > 0) {
          output.write(buffer, 0, read);
        } else {
          output.write(buffer, 0, (int) toRead + read);
          break;
        }
      }
    }
  }

  private static void close(Closeable resource) {
    if (resource != null) {
      try {
        resource.close();
      } catch (IOException ignore) {
      }
    }
  }

  protected class Range {
    long start;
    long end;
    long length;
    long total;

    public Range(long start, long end, long total) {
      this.start = start;
      this.end = end;
      this.length = end - start + 1;
      this.total = total;
    }
  }

}
