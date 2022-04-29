package br.net.pin.qinpel_srv.hook;

import java.io.IOException;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.work.Guard;
import br.net.pin.qinpel_srv.work.OrdersSTRs;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesSTRs {
  public static void init(ServletContextHandler context) {
    initList(context);
  }

  private static void initList(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var onWay = (Runny) req.getServletContext().getAttribute("QinServer.runny");
        var user = Guard.getAuthed(onWay, req);
        if (user == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
        resp.getWriter().print(OrdersSTRs.list(onWay, user));
      }
    }), "/list/strs");
  }
}
