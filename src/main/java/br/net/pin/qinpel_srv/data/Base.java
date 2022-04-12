package br.net.pin.qinpel_srv.data;

import com.google.gson.Gson;

public class Base {
  public String name;
  public String jdbc;
  public String user;
  public String pass;

  public void fixDefaults() {
    if (this.name == null) {
      this.name = "";
    }
    if (this.jdbc == null) {
      this.jdbc = "";
    }
    if (this.jdbc.startsWith("${env:") && this.jdbc.endsWith("}")) {
      this.jdbc = System.getenv(this.jdbc.substring(6, this.jdbc.length() - 1));
    }
    if (this.user == null) {
      this.user = "";
    }
    if (this.user.startsWith("${env:") && this.user.endsWith("}")) {
      this.user = System.getenv(this.user.substring(6, this.user.length() - 1));
    }
    if (this.pass == null) {
      this.pass = "";
    }
    if (this.pass.startsWith("${env:") && this.pass.endsWith("}")) {
      this.pass = System.getenv(this.pass.substring(6, this.pass.length() - 1));
    }
  }
  
  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Base fromString(String json) {
    return new Gson().fromJson(json, Base.class);
  }
}
