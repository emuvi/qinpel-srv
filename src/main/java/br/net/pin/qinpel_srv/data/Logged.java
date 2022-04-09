package br.net.pin.qinpel_srv.data;

import com.google.gson.Gson;

public class Logged {
  
    public String lang;
    public String token;

    public Logged(String lang, String token) {
        this.lang = lang;
        this.token = token;
    }

    @Override
    public String toString() {
      return new Gson().toJson(this);
    }

    public static Logged fromString(String json) {
      return new Gson().fromJson(json, Logged.class);
    }

}
