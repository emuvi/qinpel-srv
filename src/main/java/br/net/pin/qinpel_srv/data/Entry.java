package br.net.pin.qinpel_srv.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Entry {
  private final Air air;
  private final Map<String, Authed> tokens;
  private final AtomicLong lastClean;

  public Entry(Air air) {
    this.air = air;
    this.tokens = new ConcurrentHashMap<>();
    this.lastClean = new AtomicLong(System.currentTimeMillis());
  }

  public void putAuthed(String token, Authed authed) {
    this.tokens.put(token, authed);
  }

  public User getAuthed(String token) {
    var authed = this.tokens.get(token);
    if (authed == null) {
      return null;
    }
    if (authed.expired(this.air.setup.tokenValidity)) {
      this.tokens.remove(token);
      return null;
    }
    return authed.user;
  }

  public void clean() {
    long now = System.currentTimeMillis();
    if (now - this.lastClean.get() > this.air.setup.cleanInterval) {
      this.lastClean.set(now);
      this.tokens.entrySet().removeIf(entry -> entry.getValue().expired(
          this.air.setup.tokenValidity));
    }
  }
}
