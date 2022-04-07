package br.net.pin.qinpel_srv;

import java.util.Map;
import com.google.gson.Gson;

public class Access {

  public Type type;
  public Map<String, String> data;

  public enum Type {
    APP, DIR, CMD, DAT, REG, SQL, LIZ
  };

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Access fromString(String json) {
    return new Gson().fromJson(json, Access.class);
  }

}
