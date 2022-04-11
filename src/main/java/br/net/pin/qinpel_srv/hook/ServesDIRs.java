package br.net.pin.qinpel_srv.hook;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import br.net.pin.qinpel_srv.data.OnePath;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.work.Guard;
import br.net.pin.qinpel_srv.work.OrdersDIRs;
import br.net.pin.qinpel_srv.work.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesDIRs {

  public static void init(ServletContextHandler context) {
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
        var body = IOUtils.toString(req.getReader());
        var onePath = OnePath.fromString(body);
        if (onePath.path == null || onePath.path.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
          return;
        }
        onePath.path = Utils.fixPath(onePath.path, user.home);
        if (!Guard.allowDIR(onePath.path, user, false)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
        var path = new File(onePath.path);
        if (!path.exists()) {
          resp.sendError(HttpServletResponse.SC_NOT_FOUND);
          return;
        }
        if (!path.isDirectory()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The path is not a directory");
          return;
        }
        resp.getWriter().print(OrdersDIRs.dirList(path));
      }
    }), "/dir/list");

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
        var body = IOUtils.toString(req.getReader());
        var onePath = OnePath.fromString(body);
        if (onePath.path == null || onePath.path.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
          return;
        }
        onePath.path = Utils.fixPath(onePath.path, user.home);
        if (!Guard.allowDIR(onePath.path, user, false)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
        var path = new File(onePath.path);
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/dir/new");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/dir/copy");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/dir/move");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/dir/del");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/read");


    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/write");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/append");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/copy");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/move");

    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/del");
  }

}
