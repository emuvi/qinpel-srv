package br.net.pin.qinpel_srv.data;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

public class Setup {
  public Boolean serverVerbose;
  public Boolean serverArchive;
  public String serverName;
  public String serverHost;
  public Integer serverPort;
  public String serverFolder;
  public transient Pathed serverPathed;
  public Integer threadsMax;
  public Integer threadsMin;
  public Integer threadsIdleTimeout;
  public Boolean servesPUBs;
  public Boolean servesAPPs;
  public Boolean servesDIRs;
  public Boolean servesCMDs;
  public Boolean servesDATs;
  public Boolean servesREGs;
  public Boolean servesSQLs;
  public Boolean servesLIZs;
  public Map<String, String> redirects;

  public void fixDefaults() {
    if (this.serverVerbose == null) {
      this.serverVerbose = false;
    }
    if (this.serverArchive == null) {
      this.serverArchive = false;
    }
    if (this.serverName == null) {
      this.serverName = "QinpelSrv";
    }
    if (this.serverHost == null) {
      this.serverHost = "localhost";
    }
    if (this.serverPort == null) {
      this.serverPort = 5490;
    }
    if (this.serverFolder == null) {
      this.serverFolder = "";
    }
    this.serverPathed = new Pathed(this.serverFolder);
    if (this.threadsMax == null) {
      this.threadsMax = 100;
    }
    if (this.threadsMin == null) {
      this.threadsMin = 10;
    }
    if (this.threadsIdleTimeout == null) {
      this.threadsIdleTimeout = 120;
    }
    if (this.servesPUBs == null) {
      this.servesPUBs = false;
    }
    if (this.servesAPPs == null) {
      this.servesAPPs = false;
    }
    if (this.servesDIRs == null) {
      this.servesDIRs = false;
    }
    if (this.servesCMDs == null) {
      this.servesCMDs = false;
    }
    if (this.servesDATs == null) {
      this.servesDATs = false;
    }
    if (this.servesREGs == null) {
      this.servesREGs = false;
    }
    if (this.servesSQLs == null) {
      this.servesSQLs = false;
    }
    if (this.servesLIZs == null) {
      this.servesLIZs = false;
    }
    if (this.redirects == null) {
      this.redirects = new HashMap<>();
    }
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Setup fromString(String source) {
    return new Gson().fromJson(source, Setup.class);
  }
}