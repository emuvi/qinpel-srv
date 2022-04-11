package br.net.pin.qinpel_srv.data;

public class Pathed {
  private String path;

  public Pathed(String path) {
    if (path == null) {
      path = "";
    }
    this.path = path.replace("\\", "/");
  }
}
