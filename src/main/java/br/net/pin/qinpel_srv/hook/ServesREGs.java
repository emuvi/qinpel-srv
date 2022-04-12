package br.net.pin.qinpel_srv.hook;

import java.io.IOException;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesREGs {
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
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/reg/new");
  }

  private static void initRegAsk(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/reg/ask");
  }

  private static void initRegSet(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/reg/set");
  }

  private static void initRegDel(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/reg/del");
  }
}
