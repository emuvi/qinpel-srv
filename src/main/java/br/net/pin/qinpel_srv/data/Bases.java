package br.net.pin.qinpel_srv.data;

import java.util.ArrayList;
import com.google.gson.Gson;

public class Bases extends ArrayList<Base> {
  public void fixDefaults() {
    for (var base : this) {
      base.fixDefaults();
    }
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Bases fromString(String json) {
    return new Gson().fromJson(json, Bases.class);
  }
}
