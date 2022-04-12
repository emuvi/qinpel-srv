package br.net.pin.qinpel_srv.data;

import java.util.List;
import com.google.gson.Gson;

public class ArgsInputs {
  public List<String> args;
  public List<String> inputs;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static ArgsInputs fromString(String json) {
    return new Gson().fromJson(json, ArgsInputs.class);
  }
}
