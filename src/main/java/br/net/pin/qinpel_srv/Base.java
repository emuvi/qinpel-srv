package br.net.pin.qinpel_srv;

import com.google.gson.Gson;

public class Base {

  public String name;
  public String link;

  public void fixDefaults() {
    if (this.name == null) {
      this.name = "";
    }
    if (this.link == null) {
      this.link = "";
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
