package br.net.pin.qinpel_srv.swop;

import java.util.List;
import com.google.gson.Gson;
import br.net.pin.batx.data.Clause;

public class RegAsk {
  public String store;
  public String name;
  public List<String> takes;
  public List<Clause> where;
  public List<String> sorts;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static RegNew fromString(String json) {
    return new Gson().fromJson(json, RegNew.class);
  }
}