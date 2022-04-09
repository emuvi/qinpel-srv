package br.net.pin.qinpel_srv.hook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.work.Guard;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesAPPs {

  public static void init(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var reqFile = URLDecoder.decode(req.getPathInfo(), "UTF-8");
        var file = new File("app", reqFile);
        if (!file.exists()) {
          resp.sendError(HttpServletResponse.SC_NOT_FOUND);
          return;
        }
        if (reqFile.startsWith("/qinpel-app/")) {
          sendAppSrc(file, resp);
          return;
        }
        var onWay = (Runny) req.getServletContext().getAttribute("QinServer.runny");
        var user = Guard.getUser(onWay, req);
        if (user == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
        resp.getWriter().print(onWay.setup.serverHost);
      }
    }), "/app/*");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/list/apps");
  }

  private static void sendAppSrc(File file, HttpServletResponse resp) throws IOException {
    resp.setContentLength((int) file.length());
    IOUtils.copy(new FileInputStream(file), resp.getOutputStream());
  }

}
