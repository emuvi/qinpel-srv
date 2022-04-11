package br.net.pin.qinpel_srv.hook;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.work.Guard;
import br.net.pin.qinpel_srv.work.OrdersAPPs;
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
        var onWay = (Runny) req.getServletContext().getAttribute("QinServer.runny");
        var reqURL = req.getPathInfo();
        if (reqURL == null || reqURL.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
          return;
        }
        reqURL = URLDecoder.decode(reqURL, "UTF-8");
        var reqFile = new File(onWay.setup.serverFolder, "app" + reqURL);
        if (!reqFile.exists()) {
          resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no file: "
              + reqFile);
          return;
        }
        if (reqURL.startsWith("/qinpel-app/")) {
          OrdersAPPs.send(reqFile, resp);
          return;
        }
        var user = Guard.getUser(onWay, req);
        if (user == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
          return;
        }
        var appName = reqURL.substring(1);
        var idxSlash = appName.indexOf('/');
        if (idxSlash != -1) {
          appName = appName.substring(0, idxSlash);
        }
        if (!Guard.allowAPP(appName, user)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to the application: " + appName);
          return;
        }
        OrdersAPPs.send(reqFile, resp);
      }
    }), "/app/*");

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
        resp.getWriter().print(OrdersAPPs.listAPPs(onWay, user));
      }
    }), "/list/apps");
  }
}
