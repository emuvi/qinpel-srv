package br.net.pin.qinpel_srv.data;

import java.io.PrintWriter;
import org.apache.commons.io.FilenameUtils;

public class Runny {
  public final Setup setup;
  public final Users users;
  public final Bases bases;
  public final Tokens tokens;
  private final PrintWriter archive;

  public Runny(Setup setup, Users users, Bases bases) throws Exception {
    this.setup = setup;
    this.users = users;
    this.bases = bases;
    this.tokens = new Tokens(setup);
    this.archive = setup.serverArchive ? new PrintWriter(setup.serverFolder + "/"
        + setup.serverName + ".log") : null;
  }

  public void logInfo(String message) {
    message = logMake("INFO", message, null);
    if (this.setup.serverVerbose) {
      System.out.print(message);
      System.out.flush();
    }
    if (this.archive != null) {
      this.archive.print(message);
      this.archive.flush();
    }
  }

  public void logWarn(String message) {
    message = logMake("WARN", message, this);
    if (this.setup.serverVerbose) {
      System.out.print(message);
      System.out.flush();
    }
    if (this.archive != null) {
      this.archive.print(message);
      this.archive.flush();
    }
  }

  public void logErro(Throwable error) {
    var message = logMake("ERRO", error.getMessage(), error);
    if (this.setup.serverVerbose) {
      System.out.print(message);
      System.out.flush();
    }
    if (this.archive != null) {
      this.archive.print(message);
      this.archive.flush();
    }
  }

  private String logMake(String kind, String message, Object traceOf) {
    var result = new StringBuilder();
    result.append("[");
    result.append(kind);
    result.append("] ");
    result.append(message);
    if (traceOf != null) {
      if (traceOf instanceof Throwable error) {
        result.append(this.getOrigin(error));
      } else {
        result.append(this.getOrigin());
      }
    }
    result.append("\n");
    return result.toString();
  }

  private String getOrigin() {
    StringBuilder builder = new StringBuilder();
    builder.append(" {");
    StackTraceElement elements[] = Thread.currentThread().getStackTrace();
    for (int i = elements.length - 1; i >= 0; i--) {
      StackTraceElement element = elements[i];
      if (element.getClassName().startsWith("br.net.pin.qinpel_srv") && !element
          .getClassName().equals("br.net.pin.qinpel_srv.data.Runny")) {
        builder.append(" |> ");
        builder.append(FilenameUtils.getBaseName(element.getFileName()));
        builder.append("[");
        builder.append(element.getLineNumber());
        builder.append("](");
        builder.append(element.getMethodName());
        builder.append(")");
      }
    }
    builder.append("}");
    return builder.toString();
  }

  private String getOrigin(Throwable ofError) {
    StringBuilder builder = new StringBuilder();
    builder.append(" {");
    StackTraceElement elements[] = ofError.getStackTrace();
    for (int i = elements.length - 1; i >= 0; i--) {
      StackTraceElement element = elements[i];
      if (element.getClassName().startsWith("br.net.pin.qinpel_srv") && !element
          .getClassName().equals("br.net.pin.qinpel_srv.data.Runny")) {
        builder.append(" |> ");
        builder.append(FilenameUtils.getBaseName(element.getFileName()));
        builder.append("[");
        builder.append(element.getLineNumber());
        builder.append("](");
        builder.append(element.getMethodName());
        builder.append(")");
      }
    }
    builder.append("}");
    return builder.toString();
  }
}

