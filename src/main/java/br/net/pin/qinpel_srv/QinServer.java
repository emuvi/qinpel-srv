package br.net.pin.qinpel_srv;

import java.io.File;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class QinServer {

  private final SrvData srvData;
  private final QueuedThreadPool threadPool;
  private final Server server;
  private final HttpConfiguration httpConfig;
  private final HttpConnectionFactory httpFactory;
  private final ServerConnector connector;
  private final ServletContextHandler context;

  public QinServer(SrvData srvData) {
    this.srvData = srvData;
    this.threadPool = new QueuedThreadPool(this.srvData.setup.threadsMax,
        this.srvData.setup.threadsMin, this.srvData.setup.threadsIdleTimeout);
    this.server = new Server(this.threadPool);
    this.httpConfig = new HttpConfiguration();
    this.httpConfig.setSendDateHeader(false);
    this.httpConfig.setSendServerVersion(false);
    this.httpFactory = new HttpConnectionFactory(this.httpConfig);
    this.connector = new ServerConnector(this.server, httpFactory);
    connector.setHost(this.srvData.setup.serverHost);
    connector.setPort(this.srvData.setup.serverPort);
    this.server.setConnectors(new Connector[] {this.connector});
    this.context = new ServletContextHandler();
    this.context.setContextPath("/");
    this.context.setAttribute("QinServer.srvData", this.srvData);
    this.server.setHandler(this.context);
    this.init_serves();
  }

  private void init_serves() {
    if (this.srvData.setup.servesPUBs) {
      this.serves_pubs();
    }
    if (this.srvData.setup.servesAPPs) {
      this.serves_apps();
    }
    if (this.srvData.setup.servesDIRs) {
      this.serves_dirs();
    }
    if (this.srvData.setup.servesCMDs) {
      this.serves_cmds();
    }
    if (this.srvData.setup.servesDATs) {
      this.serves_dats();
    }
    if (this.srvData.setup.servesREGs) {
      this.serves_regs();
    }
    if (this.srvData.setup.servesSQLs) {
      this.serves_sqls();
    }
    if (this.srvData.setup.servesLIZs) {
      this.serves_lizs();
    }
    this.serves_utils();
  }

  private void serves_pubs() {
    System.out.println("Serving PUBs...");
    var holder = new ServletHolder(new ServesPUBs());
    holder.setInitParameter("basePath", new File("./pub").getAbsolutePath());
    this.context.addServlet(holder, "/pub/*");
  }

  private void serves_apps() {
    System.out.println("Serving APPs...");
    ServesAPPs.init(this.context);
  }

  private void serves_dirs() {
    System.out.println("Serving DIRs...");
    ServesDIRs.init(this.context);
  }

  private void serves_cmds() {
    System.out.println("Serving CMDs...");
    ServesCMDs.init(this.context);
  }

  private void serves_dats() {
    System.out.println("Serving DATs...");
    ServesDATs.init(this.context);
  }

  private void serves_regs() {
    System.out.println("Serving REGs...");
    ServesREGs.init(this.context);
  }

  private void serves_sqls() {
    System.out.println("Serving SQLs...");
    ServesSQLs.init(this.context);
  }

  private void serves_lizs() {
    System.out.println("Serving LIZs...");
    ServesLIZs.init(this.context);
  }

  private void serves_utils() {
    System.out.println("Serving Utils...");
    ServesUtils.init(this.context);
  }

  public void start() throws Exception {
    this.server.start();
    this.server.join();
  }

}

