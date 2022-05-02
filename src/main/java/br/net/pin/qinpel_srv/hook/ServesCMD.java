package br.net.pin.qinpel_srv.hook;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import br.net.pin.qinpel_srv.swap.Execute;
import br.net.pin.qinpel_srv.work.OrdersCMD;
import br.net.pin.qinpel_srv.work.Runner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesCMD {
  public static void init(ServletContextHandler context) {
    initRun(context);
    initList(context);
  }

  private static void initRun(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var execute = Execute.fromString(body);
        if (execute.exec == null || execute.exec.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a executable");
          return;
        }
        if (!authed.allowCMD(execute.exec)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to the command: " + execute.exec);
          return;
        }
        var reqDir = new File(way.air.setup.serverFolder, "cmd/" + execute.exec);
        if (!reqDir.exists()) {
          resp.sendError(HttpServletResponse.SC_NOT_FOUND,
              "There is no command on folder: " + reqDir);
          return;
        }
        var executable = new File(reqDir, execute.exec + ".jar");
        if (!executable.exists()) {
          executable = new File(reqDir, execute.exec + ".exe");
        }
        if (!executable.exists()) {
          executable = new File(reqDir, execute.exec);
        }
        if (!executable.exists()) {
          resp.sendError(HttpServletResponse.SC_NOT_FOUND,
              "There is no command at executable: " + executable);
          return;
        }
        try {
          resp.getWriter().print(OrdersCMD.run(executable, execute));
          resp.setContentType("text/plain");
        } catch (Exception e) {
          throw new ServletException(e);
        }
      }
    }), "/cmd/run");
  }

  private static void initList(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
          return;
        }
        resp.setContentType("text/plain");
        resp.getWriter().print(OrdersCMD.list(way, authed));
      }
    }), "/list/cmds");
  }
}
