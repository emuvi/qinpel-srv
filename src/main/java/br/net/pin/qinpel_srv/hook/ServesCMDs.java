package br.net.pin.qinpel_srv.hook;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import br.net.pin.qinpel_srv.data.ArgsInputs;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.work.Guard;
import br.net.pin.qinpel_srv.work.OrdersCMDs;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesCMDs {
  public static void init(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var onWay = (Runny) req.getServletContext().getAttribute("QinServer.runny");
        var reqURL = req.getPathInfo();
        if (reqURL == null || reqURL.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a path name");
          return;
        }
        var user = Guard.getUser(onWay, req);
        if (user == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
          return;
        }
        reqURL = URLDecoder.decode(reqURL, "UTF-8");
        var cmdName = reqURL.substring(1);
        var idxSlash = cmdName.indexOf('/');
        if (idxSlash != -1) {
          cmdName = cmdName.substring(0, idxSlash);
        }
        if (!Guard.allowAPP(cmdName, user)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to the command: " + cmdName);
          return;
        }
        var reqDir = new File(onWay.setup.serverFolder, "cmd/" + cmdName);
        if (!reqDir.exists()) {
          resp.sendError(HttpServletResponse.SC_NOT_FOUND,
              "There is no command on folder: " + reqDir);
          return;
        }
        var executable = new File(reqDir, cmdName + ".jar");
        if (!executable.exists()) {
          executable = new File(reqDir, cmdName + ".exe");
        }
        if (!executable.exists()) {
          executable = new File(reqDir, cmdName);
        }
        if (!executable.exists()) {
          resp.sendError(HttpServletResponse.SC_NOT_FOUND,
              "There is no command at executable: " + executable);
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var argsInputs = ArgsInputs.fromString(body);
        try {
          resp.getWriter().print(OrdersCMDs.run(executable, argsInputs));  
        } catch (Exception e) {
          throw new ServletException(e);
        }
      }
    }), "/cmd/*");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var onWay = (Runny) req.getServletContext().getAttribute("QinServer.runny");
        var user = Guard.getUser(onWay, req);
        if (user == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
        resp.getWriter().print(OrdersCMDs.list(onWay, user));
      }
    }), "/list/cmds");
  }
}
