package br.net.pin.qinpel_srv.data;

public class Air {
  public final Setup setup;
  public final Users users;
  public final Bases bases;
  
  public Air(Setup setup, Users users, Bases bases) throws Exception {
    this.setup = setup;
    this.users = users;
    this.bases = bases;
  }
}