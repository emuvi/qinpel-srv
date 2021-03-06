package br.net.pin.qinpel_srv.hook;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import br.net.pin.jabx.data.Deed;
import br.net.pin.jabx.data.Delete;
import br.net.pin.jabx.data.Insert;
import br.net.pin.jabx.data.Select;
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
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        if (authed == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
          return;
        }
        var body = IOUtils.toString(req.getReader());
        var insert = Insert.fromString(body);
        if (insert.registry == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry");
          return;
        }
        if (insert.registry.base == null || insert.registry.base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry base");
          return;
        }
        if (insert.registry.name == null || insert.registry.name.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry name");
          return;
        }
        if (!authed.allowREG(insert.registry, Deed.INSERT)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.setContentType("text/plain");
        resp.getWriter().print(OrdersREG.regNew(way, insert));
      }
    }), "/reg/new");
  }

  private static void initRegAsk(ServletContextHandler context) {
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
        var select = Select.fromString(body);
        if (select.registry == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry");
          return;
        }
        if (select.registry.base == null || select.registry.base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry base");
          return;
        }
        if (select.registry.name == null || select.registry.name.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry name");
          return;
        }
        if (!authed.allowREG(select.registry, Deed.SELECT)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.setContentType("text/plain");
        resp.getWriter().print(OrdersREG.regAsk(way, select));
      }
    }), "/reg/ask");
  }

  private static void initRegSet(ServletContextHandler context) {
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
        var update = Update.fromString(body);
        if (update.registry == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry");
          return;
        }
        if (update.registry.base == null || update.registry.base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry base");
          return;
        }
        if (update.registry.name == null || update.registry.name.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry name");
          return;
        }
        if (!authed.allowREG(update.registry, Deed.UPDATE)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.setContentType("text/plain");
        resp.getWriter().print(OrdersREG.regSet(way, update));
      }
    }), "/reg/set");
  }

  private static void initRegDel(ServletContextHandler context) {
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
        var delete = Delete.fromString(body);
        if (delete.registry == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry");
          return;
        }
        if (delete.registry.base == null || delete.registry.base.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry base");
          return;
        }
        if (delete.registry.name == null || delete.registry.name.isEmpty()) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
              "You must provide a registry name");
          return;
        }
        if (!authed.allowREG(delete.registry, Deed.DELETE)) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "You don't have access to deed this registry");
          return;
        }
        resp.setContentType("text/plain");
        resp.getWriter().print(OrdersREG.regDel(way, delete));
      }
    }), "/reg/del");
  }
}
