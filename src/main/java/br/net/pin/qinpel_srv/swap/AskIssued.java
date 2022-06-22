package br.net.pin.qinpel_srv.swap;

import com.google.gson.Gson;

public class AskIssued {
  public String token;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static AskIssued fromString(String json) {
    return new Gson().fromJson(json, AskIssued.class);
  }
}
