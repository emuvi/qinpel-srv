package br.net.pin.qinpel_srv.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Runny {
  public final Setup setup;
  public final Users users;
  public final Bases bases;
  public final Map<String, Authed> tokens;
  public final AtomicLong lastClean;

  public Runny(Setup setup, Users users, Bases bases) {
    this.setup = setup;
    this.users = users;
    this.bases = bases;
    this.tokens = new ConcurrentHashMap<>();
    this.lastClean = new AtomicLong(System.currentTimeMillis());
  }
}
