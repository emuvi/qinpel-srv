package br.net.pin.qinpel_srv.hook;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.swop.TryAuth;
import br.net.pin.qinpel_srv.work.Login;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServerAuth {
  public static void init(ServletContextHandler context) {
    initEnter(context);
  }

  private static void initEnter(ServletContextHandler context) {
    context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var onWay = (Runny) req.getServletContext().getAttribute("QinServer.runny");
        onWay.entry.clean();
        var body = IOUtils.toString(req.getReader());
        var tryAuth = TryAuth.fromString(body);
        var logged = Login.tryEnter(tryAuth, onWay);
        if (logged == null) {
          resp.sendError(HttpServletResponse.SC_FORBIDDEN,
              "The user and/or pass is incorrect.");
          return;
        }
        resp.getWriter().print(logged.toString());
      }
    }), "/enter");
  }
}
