package br.net.pin.qinpel_srv.data;

import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Runny {
  public final Setup setup;
  public final Users users;
  public final Bases bases;

  private final PrintWriter archive;
  private final Map<String, Authed> tokens;
  private final AtomicLong lastClean;

  public Runny(Setup setup, Users users, Bases bases) throws Exception {
    this.setup = setup;
    this.users = users;
    this.bases = bases;
    this.archive = setup.serverArchive ? new PrintWriter(setup.serverFolder + "/" + setup.serverName + ".log") : null;
    this.tokens = new ConcurrentHashMap<>();
    this.lastClean = new AtomicLong(System.currentTimeMillis());
  }

  public void logInfo(String message) {
    message = logMake("INFO", message, null);
    if (this.setup.serverVerbose) {
      System.out.print(message);
      System.out.flush();
    }
    if (this.archive != null) {
      this.archive.print(message);
      this.archive.flush();
    }
  }

  public void logWarn(String message) {
    message = logMake("WARN", message, this);
    if (this.setup.serverVerbose) {
      System.out.print(message);
      System.out.flush();
    }
    if (this.archive != null) {
      this.archive.print(message);
      this.archive.flush();
    }
  }

  public void logErro(Throwable error) {
    var message = logMake("ERRO", error.getMessage(), error);
    if (this.setup.serverVerbose) {
      System.out.print(message);
      System.out.flush();
    }
    if (this.archive != null) {
        this.archive.print(message);
        this.archive.flush();
    }
  }

  private String logMake(String kind, String message, Object traceOf) {
    var result = new StringBuilder();
    result.append("[");
    result.append(kind);
    result.append("] ");
    result.append(message);
    result.append("\n");
    return result.toString();
  }

  public void putAuthed(String token, Authed authed) {
    this.tokens.put(token, authed);
  }

  public User getAuthed(String token) {
    var authed = this.tokens.get(token);
    if (authed == null) {
      return null;
    }
    if (authed.expired(this.setup.tokenValidity)) {
      this.tokens.remove(token);
      return null;
    }
    return authed.user;
  }

  public void clean() {
    long now = System.currentTimeMillis();
    if (now - this.lastClean.get() > this.setup.cleanInterval) {
      this.lastClean.set(now);
      this.tokens.entrySet().removeIf(entry -> entry.getValue().expired(this.setup.tokenValidity));
    }
  }
}

