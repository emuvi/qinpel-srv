package br.net.pin.qinpel_srv.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class User {
  public String name;
  public String pass;
  public String home;
  public String lang;
  public Boolean master;
  public List<Access> access;

  public void fixDefaults() {
    if (this.name == null) {
      this.name = "";
    }
    if (this.pass == null) {
      this.pass = "";
    }
    if (this.home == null) {
      this.home = "";
    }
    if (this.lang == null) {
      this.lang = "";
    }
    if (this.master == null) {
      this.master = false;
    }
    if (this.access == null) {
      this.access = new ArrayList<>();
    }
    for (var access : this.access) {
      access.fixDefaults();
    }
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static User fromString(String json) {
    return new Gson().fromJson(json, User.class);
  }
}
