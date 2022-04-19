package br.net.pin.qinpel_srv.swop;

import java.util.List;
import com.google.gson.Gson;
import br.net.pin.jabx.data.Clause;

public class RegDel {
  public String store;
  public String name;
  public List<Clause> where;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static RegNew fromString(String json) {
    return new Gson().fromJson(json, RegNew.class);
  }
}
