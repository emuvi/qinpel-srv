package br.net.pin.qinpel_srv.data;

import java.io.File;
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
  public Boolean servesPUBs;
  public Boolean servesAPPs;
  public Boolean servesDIRs;
  public Boolean servesCMDs;
  public Boolean servesSTRs;
  public Boolean servesREGs;
  public Boolean servesSQLs;
  public Boolean servesLIZs;
  public Boolean servesGIZs;

  public Map<String, String> redirects;

  public Integer threadsMin;
  public Integer threadsMax;
  public Integer threadsIdleTimeout;
  public Long cleanInterval;
  public Long tokenValidity;
  public Integer storeMinIdle;
  public Integer storeMaxIdle;
  public Integer storeMaxTotal;

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
    this.serverFolder = new File(this.serverFolder).getAbsolutePath();
    
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
    if (this.servesSTRs == null) {
      this.servesSTRs = false;
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
    if (this.servesGIZs == null) {
      this.servesGIZs = false;
    }

    if (this.redirects == null) {
      this.redirects = new HashMap<>();
    }

    if (this.threadsMin == null) {
      this.threadsMin = 10;
    }
    if (this.threadsMax == null) {
      this.threadsMax = 100;
    }
    if (this.threadsIdleTimeout == null) {
      this.threadsIdleTimeout = 120;
    }
    if (this.cleanInterval == null) {
      this.cleanInterval = 12 * 60 * 60 * 1000L;
    }
    if (this.tokenValidity == null) {
      this.tokenValidity = 24 * 60 * 60 * 1000L;
    }
    if (this.storeMinIdle == null) {
      this.storeMinIdle = 10;
    }
    if (this.storeMaxIdle == null) {
      this.storeMaxIdle = 30;
    }
    if (this.storeMaxTotal == null) {
      this.storeMaxTotal = 100;
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
