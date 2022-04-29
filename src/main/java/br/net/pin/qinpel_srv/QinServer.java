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
import br.net.pin.qinpel_srv.hook.ServesAPP;
import br.net.pin.qinpel_srv.hook.ServesBAS;
import br.net.pin.qinpel_srv.hook.ServesCMD;
import br.net.pin.qinpel_srv.hook.ServesDIR;
import br.net.pin.qinpel_srv.hook.ServesGIZ;
import br.net.pin.qinpel_srv.hook.ServesLIZ;
import br.net.pin.qinpel_srv.hook.ServesPUB;
import br.net.pin.qinpel_srv.hook.ServesREG;
import br.net.pin.qinpel_srv.hook.ServesSQL;

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
    this.threadPool = new QueuedThreadPool(this.runny.air.setup.threadsMax,
        this.runny.air.setup.threadsMin, this.runny.air.setup.threadsIdleTimeout);
    this.server = new Server(this.threadPool);
    this.httpConfig = new HttpConfiguration();
    this.httpConfig.setSendDateHeader(false);
    this.httpConfig.setSendServerVersion(false);
    this.httpFactory = new HttpConnectionFactory(this.httpConfig);
    this.connector = new ServerConnector(this.server, httpFactory);
    connector.setHost(this.runny.air.setup.serverHost);
    connector.setPort(this.runny.air.setup.serverPort);
    this.server.setConnectors(new Connector[] { this.connector });
    this.context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    this.context.setContextPath("");
    this.context.setAttribute("QinServer.runny", this.runny);
    this.server.setHandler(this.context);
    this.init_serves();
  }

  private void init_serves() throws Exception {
    this.server_auth();
    if (this.runny.air.setup.servesPUB) {
      this.serves_pubs();
    }
    if (this.runny.air.setup.servesAPP) {
      this.serves_apps();
    }
    if (this.runny.air.setup.servesDIR) {
      this.serves_dirs();
    }
    if (this.runny.air.setup.servesCMD) {
      this.serves_cmds();
    }
    if (this.runny.air.setup.servesBAS) {
      this.serves_strs();
    }
    if (this.runny.air.setup.servesBAS && this.runny.air.setup.servesREG) {
      this.serves_regs();
    }
    if (this.runny.air.setup.servesBAS && this.runny.air.setup.servesSQL) {
      this.serves_sqls();
    }
    if (this.runny.air.setup.servesLIZ) {
      this.serves_lizs();
    }
    if (this.runny.air.setup.servesGIZ) {
      this.serves_gizs();
    }
    this.server_utils();
  }

  private void server_auth() {
    this.runny.logInfo("Serving Auths...");
    ServerAuth.init(this.context);
  }

  private void serves_pubs() throws Exception {
    this.runny.logInfo("Serving PUBs...");
    var holder = new ServletHolder(new ServesPUB());
    var pubDir = new File("pub");
    if (!pubDir.exists()) {
      Files.createDirectories(pubDir.toPath());
    }
    holder.setInitParameter("basePath", pubDir.getAbsolutePath());
    this.context.addServlet(holder, "/pub/*");
  }

  private void serves_apps() {
    this.runny.logInfo("Serving APPs...");
    ServesAPP.init(this.context);
  }

  private void serves_dirs() {
    this.runny.logInfo("Serving DIRs...");
    ServesDIR.init(this.context);
  }

  private void serves_cmds() {
    this.runny.logInfo("Serving CMDs...");
    ServesCMD.init(this.context);
  }

  private void serves_strs() {
    this.runny.logInfo("Serving STRs...");
    ServesBAS.init(this.context);
  }

  private void serves_regs() {
    this.runny.logInfo("Serving REGs...");
    ServesREG.init(this.context);
  }

  private void serves_sqls() {
    this.runny.logInfo("Serving SQLs...");
    ServesSQL.init(this.context);
  }

  private void serves_lizs() {
    this.runny.logInfo("Serving LIZs...");
    ServesLIZ.init(this.context);
  }

  private void serves_gizs() {
    this.runny.logInfo("Serving GIZs...");
    ServesGIZ.init(this.context);
  }

  private void server_utils() {
    this.runny.logInfo("Serving Utils...");
    ServerUtils.init(this.context, this.runny.air.setup);
  }

  public void start() throws Exception {
    this.runny.logInfo("Starting Server...");
    this.runny.logInfo("Setup On-Air: " + this.runny.air.setup);
    this.runny.logInfo("Users On-Air: " + this.runny.air.users);
    this.runny.logInfo("Bases On-Air: " + this.runny.air.bases);
    this.server.start();
    this.server.join();
  }

}
