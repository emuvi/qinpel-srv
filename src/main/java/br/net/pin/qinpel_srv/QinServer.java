package br.net.pin.qinpel_srv;

import java.io.File;
import java.nio.file.Files;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.hook.ServerAuth;
import br.net.pin.qinpel_srv.hook.ServerUtils;
import br.net.pin.qinpel_srv.hook.ServesAPPs;
import br.net.pin.qinpel_srv.hook.ServesCMDs;
import br.net.pin.qinpel_srv.hook.ServesDATs;
import br.net.pin.qinpel_srv.hook.ServesDIRs;
import br.net.pin.qinpel_srv.hook.ServesLIZs;
import br.net.pin.qinpel_srv.hook.ServesPUBs;
import br.net.pin.qinpel_srv.hook.ServesREGs;
import br.net.pin.qinpel_srv.hook.ServesSQLs;

public class QinServer {

  private final Runny runny;
  private final QueuedThreadPool threadPool;
  private final Server server;
  private final HttpConfiguration httpConfig;
  private final HttpConnectionFactory httpFactory;
  private final ServerConnector connector;
  private final ServletContextHandler context;

  public QinServer(Runny runny) throws Exception {
    this.runny = runny;
    this.threadPool = new QueuedThreadPool(this.runny.setup.threadsMax,
        this.runny.setup.threadsMin, this.runny.setup.threadsIdleTimeout);
    this.server = new Server(this.threadPool);
    this.httpConfig = new HttpConfiguration();
    this.httpConfig.setSendDateHeader(false);
    this.httpConfig.setSendServerVersion(false);
    this.httpFactory = new HttpConnectionFactory(this.httpConfig);
    this.connector = new ServerConnector(this.server, httpFactory);
    connector.setHost(this.runny.setup.serverHost);
    connector.setPort(this.runny.setup.serverPort);
    this.server.setConnectors(new Connector[] {this.connector});
    this.context = new ServletContextHandler();
    this.context.setContextPath("");
    this.context.setAttribute("QinServer.runny", this.runny);
    this.server.setHandler(this.context);
    this.init_serves();
  }

  private void init_serves() throws Exception {
    this.server_auth();
    if (this.runny.setup.servesPUBs) {
      this.serves_pubs();
    }
    if (this.runny.setup.servesAPPs) {
      this.serves_apps();
    }
    if (this.runny.setup.servesDIRs) {
      this.serves_dirs();
    }
    if (this.runny.setup.servesCMDs) {
      this.serves_cmds();
    }
    if (this.runny.setup.servesDATs) {
      this.serves_dats();
    }
    if (this.runny.setup.servesREGs) {
      this.serves_regs();
    }
    if (this.runny.setup.servesSQLs) {
      this.serves_sqls();
    }
    if (this.runny.setup.servesLIZs) {
      this.serves_lizs();
    }
    this.server_utils();
  }

  private void server_auth() {
    System.out.println("Serving Auths...");
    ServerAuth.init(this.context);
  }

  private void serves_pubs() throws Exception {
    System.out.println("Serving PUBs...");
    var holder = new ServletHolder(new ServesPUBs());
    var pubDir = new File("pub");
    if (!pubDir.exists()) {
      Files.createDirectories(pubDir.toPath());
    }
    holder.setInitParameter("basePath", pubDir.getAbsolutePath());
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

  private void server_utils() {
    System.out.println("Serving Utils...");
    ServerUtils.init(this.context, this.runny.setup);
  }

  public void start() throws Exception {
    if (this.runny.setup.serverVerbose) {
      System.out.println("Starting Server...");
      System.out.println("Setup: " + this.runny.setup);
      System.out.println("Users: " + this.runny.users);
      System.out.println("Bases: " + this.runny.bases);
    }
    this.server.start();
    this.server.join();
  }

}

