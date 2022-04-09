package br.net.pin.qinpel_srv.data;

import java.util.ArrayList;
import com.google.gson.Gson;

public class Users extends ArrayList<User> {

  public void fixDefaults() {
    var hasRoot = false;
    for (var user : this) {
      user.fixDefaults();
      if (user.name.equals("root")) {
        hasRoot = true;
      }
    }
    if (!hasRoot) {
      var root = new User();
      root.name = "root";
      root.pass = "r001";
      root.home = "./dir/root";
      root.lang = "";
      root.master = true;
      root.access = new ArrayList<>();
      this.add(root);
    }
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Users fromString(String json) {
    return new Gson().fromJson(json, Users.class);
  }

}
