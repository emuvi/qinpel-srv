package br.net.pin.qinpel_srv;

public class SrvData {
  
  public final Setup setup;
  public final Users users;
  public final Bases bases;

  public SrvData(Setup setup, Users users, Bases bases) {
    this.setup = setup;
    this.users = users;
    this.bases = bases;
  }  

}
