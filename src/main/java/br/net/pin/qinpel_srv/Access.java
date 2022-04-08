package br.net.pin.qinpel_srv;

import java.util.List;
import com.google.gson.Gson;

public class Access {

  public APP app;
  public DIR dir;
  public CMD cmd;
  public DAT dat;
  public REG reg;
  public SQL sql;
  public LIZ liz;

  public class APP {
    public String name;
  }

  public class DIR {
    public String path;
    public Boolean canWrite;
  }

  public class CMD {
    public String name;
    public List<String> args;
  }

  public class DAT {
    public String name;
  }

  public class REG {
    public String name;
  }

  public class SQL {
    public String path;
  }

  public class LIZ {
    public String path;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Access fromString(String json) {
    return new Gson().fromJson(json, Access.class);
  }

}
