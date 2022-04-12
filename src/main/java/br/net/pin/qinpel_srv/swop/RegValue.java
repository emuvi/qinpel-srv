package br.net.pin.qinpel_srv.swop;

import com.google.gson.Gson;
import br.net.pin.batx.Nature;

public class RegValue {
  public String name;
  public String data;
  public Nature type;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static RegValue fromString(String json) {
    return new Gson().fromJson(json, RegValue.class);
  }
}
