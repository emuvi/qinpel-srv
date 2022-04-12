package br.net.pin.qinpel_srv.swop;

import com.google.gson.Gson;
import br.net.pin.batx.Nature;
import br.net.pin.batx.DataLike;
import br.net.pin.batx.DataLikeTie;

public class RegWhere {
  public Boolean not;
  public String name;
  public DataLike like;
  public String data;
  public Nature type;
  public DataLikeTie tie;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static RegWhere fromString(String json) {
    return new Gson().fromJson(json, RegWhere.class);
  }
}
