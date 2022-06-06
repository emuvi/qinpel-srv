package br.net.pin.qinpel_srv.hook;

import java.io.IOException;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import br.net.pin.qinpel_srv.data.Setup;
import br.net.pin.qinpel_srv.work.Runner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServerUtils {
  public static void init(ServletContextHandler context, Setup setup) {
    initPing(context);
    initLang(context);
    initLogged(context);
    initRedirects(context, setup);
  }

  private static void initPing(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().print("pong");
      }
    }), "/ping");
  }

  private static void initLang(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var way = Runner.getWay(req);
        resp.setContentType("text/plain");
        resp.getWriter().print(way.air.setup.serverLang);
      }
    }), "/lang");
  }

  private static void initLogged(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var way = Runner.getWay(req);
        var authed = Runner.getAuthed(way, req);
        resp.setContentType("text/plain");
        if (authed != null) {
          resp.getWriter().print(authed.getUserName());
        } else {
          resp.getWriter().print("<!-- No user is logged. -->");
        }
      }
    }), "/logged");
  }

  private static void initRedirects(ServletContextHandler context, Setup setup) {
    for (var entry : setup.redirects.entrySet()) {
      context.addServlet(new ServletHolder(new HttpServlet() {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
          resp.sendRedirect(entry.getValue());
        }
      }), entry.getKey());
    }
  }
}
