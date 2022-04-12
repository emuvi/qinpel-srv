package br.net.pin.qinpel_srv.swop;

import java.util.List;
import com.google.gson.Gson;

public class RegNew {
  public String store;
  public String name;
  public List<RegValue> values;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static RegNew fromString(String json) {
    return new Gson().fromJson(json, RegNew.class);
  }
}
