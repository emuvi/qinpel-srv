package br.net.pin.qinpel_srv.hook;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import br.net.pin.jabx.data.Deed;
import br.net.pin.jabx.data.Delete;
import br.net.pin.jabx.data.Insert;
import br.net.pin.jabx.data.Update;
import br.net.pin.qinpel_srv.work.OrdersREG;
import br.net.pin.qinpel_srv.work.Runner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesREG {
  public static void init(ServletContextHandler context) {
    initRegNew(context);
    initRegAsk(context);
    initRegSet(context);
    initRegDel(context);
  }

  private static void initRegNew(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var base = req.getPathInfo().substring(1);
        if (base == null || base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a base name");
          return;
        }
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var insert = Insert.fromString(body);
        if (!authed.allowREG(base, insert.registry, Deed.INSERT)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.getWriter().print(OrdersREG.regNew(way, base, insert));
      }
    }), "/reg/new/*");
  }

  private static void initRegAsk(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var base = req.getPathInfo().substring(1);
        resp.getWriter().print("Base: " + base);
      }
    }), "/reg/ask/*");
  }

  private static void initRegSet(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var base = req.getPathInfo().substring(1);
        if (base == null || base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a base name");
          return;
        }
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var update = Update.fromString(body);
        if (!authed.allowREG(base, update.registry, Deed.UPDATE)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.getWriter().print(OrdersREG.regSet(way, base, update));
      }
    }), "/reg/set/*");
  }

  private static void initRegDel(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var base = req.getPathInfo().substring(1);
        if (base == null || base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a base name");
          return;
        }
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var delete = Delete.fromString(body);
        if (!authed.allowREG(base, delete.registry, Deed.DELETE)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.getWriter().print(OrdersREG.regDel(way, base, delete));
      }
    }), "/reg/del/*");
  }
}
