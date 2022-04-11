package br.net.pin.qinpel_srv.data;

import com.google.gson.Gson;

public class PathData {
  public String path;
  public Boolean base64;
  public String data;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static PathData fromString(String json) {
    return new Gson().fromJson(json, PathData.class);
  }
}
