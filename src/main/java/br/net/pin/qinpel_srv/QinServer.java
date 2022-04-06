package br.net.pin.qinpel_srv;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class QinServer {

  private Setup setup;
  private Server server;
  private ServletContextHandler context;

  public QinServer(Setup setup) {
    this.setup = setup;
  }

  public void start() throws Exception {
    this.setup.setDefaults();
    int maxThreads = 100;
    int minThreads = 10;
    int idleTimeout = 120;
    var threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
    this.server = new Server(threadPool);
    this.server = new Server(new InetSocketAddress(this.setup.serverHost,
        this.setup.serverPort));
    var httpConfig = new HttpConfiguration();
    httpConfig.setSendDateHeader(false);
    httpConfig.setSendServerVersion(false);
    var httpFactory = new HttpConnectionFactory(httpConfig);
    var connector = new ServerConnector(this.server, httpFactory);
    connector.setHost(this.setup.serverHost);
    connector.setPort(this.setup.serverPort);
    this.server.setConnectors(new Connector[] {connector});
    this.context = new ServletContextHandler();
    this.context.setContextPath("/");
    this.server.setHandler(this.context);
    if (this.setup.servesPUBs) {
      this.serves_pubs();
    }
    if (this.setup.servesAPPs) {
      this.serves_apps();
    }
    if (this.setup.servesDIRs) {
      this.serves_dirs();
    }
    if (this.setup.servesCMDs) {
      this.serves_cmds();
    }
    if (this.setup.servesDATs) {
      this.serves_dats();
    }
    if (this.setup.servesREGs) {
      this.serves_regs();
    }
    if (this.setup.servesSQLs) {
      this.serves_sqls();
    }
    if (this.setup.servesLIZs) {
      this.serves_lizs();
    }
    this.serves_utils();
    this.server.start();
    this.server.join();
  }

  public void serves_pubs() {
    System.out.println("Serving PUBs...");
    var holder = new ServletHolder(new ServesPUBs());
    holder.setInitParameter("basePath", new File("./pub").getAbsolutePath());
    this.context.addServlet(holder, "/pub/*");
  }

  public void serves_apps() {
    System.out.println("Serving APPs...");
    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/app/*");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/list/apps");
  }

  public void serves_dirs() {
    System.out.println("Serving DIRs...");
    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/dir/list");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/dir/new");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/dir/copy");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/dir/move");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/dir/del");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/read");


    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/write");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/append");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/copy");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/move");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/file/del");
  }

  public void serves_cmds() {
    System.out.println("Serving CMDs...");
    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/cmd/*");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/list/cmds");
  }

  public void serves_dats() {
    System.out.println("Serving DATs...");
    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/list/bases");
  }

  public void serves_regs() {
    System.out.println("Serving REGs...");
    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/reg/new/*");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/reg/ask/*");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/reg/set/*");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/reg/del/*");
  }

  public void serves_sqls() {
    System.out.println("Serving SQLs...");
    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/sql/run/*");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/sql/ask/*");
  }

  public void serves_lizs() {
    System.out.println("Serving LIZs...");
    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/liz/*");

    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        resp.getWriter().print(req.getRequestURI());
      }
    }), "/list/lizs");
  }

  public void serves_utils() {
    System.out.println("Serving Utils...");
    this.context.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        var w = resp.getWriter();
        w.write("Pong");
      }
    }), "/ping");
  }

}

