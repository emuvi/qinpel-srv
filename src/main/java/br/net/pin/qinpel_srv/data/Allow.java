package br.net.pin.qinpel_srv.data;

import java.io.File;
import java.util.List;
import com.google.gson.Gson;

public class Allow {
  public APP app;
  public DIR dir;
  public CMD cmd;
  public STR str;
  public REG reg;
  public SQL sql;
  public LIZ liz;
  public GIZ giz;

  public class APP {
    public String name;
  }

  public class DIR {
    public String path;
    public Boolean mutate;
  }

  public class CMD {
    public String name;
    public List<String> args;
  }

  public class STR {
    public String name;
  }

  public class REG {
    public String store;
    public String name;
    public Boolean mutate;
  }

  public class SQL {
    public String store;
    public String path;
  }

  public class LIZ {
    public String path;
  }

  public class GIZ {
    public String path;
  }

  public void fixDefaults() {
    if (this.app != null) {
      if (this.app.name == null || this.app.name.isEmpty()) {
        this.app = null;
      }
    }
    if (this.dir != null) {
      if (this.dir.path == null || this.dir.path.isEmpty()) {
        this.dir = null;
      } else {
        this.dir.path = new File(this.dir.path).getAbsolutePath();
        this.dir.mutate = this.dir.mutate != null ? this.dir.mutate : false;
      }
    }
    if (this.cmd != null) {
      if (this.cmd.name == null || this.cmd.name.isEmpty()) {
        this.cmd = null;
      }
    }
    if (this.str != null) {
      if (this.str.name == null || this.str.name.isEmpty()) {
        this.str = null;
      }
    }
    if (this.reg != null) {
      if (this.reg.store == null || this.reg.store.isEmpty() || this.reg.name == null
          || this.reg.name.isEmpty()) {
        this.reg = null;
      } else {
        this.reg.mutate = this.reg.mutate != null ? this.reg.mutate : false;
      }
    }
    if (this.sql != null) {
      if (this.sql.store == null || this.sql.store.isEmpty() || this.sql.path == null
          || this.sql.path.isEmpty()) {
        this.sql = null;
      } else {
        this.sql.path = new File(this.sql.path).getAbsolutePath();
      }
    }
    if (this.liz != null) {
      if (this.liz.path == null || this.liz.path.isEmpty()) {
        this.liz = null;
      } else {
        this.liz.path = new File(this.liz.path).getAbsolutePath();
      }
    }
    if (this.giz != null) {
      if (this.giz.path == null || this.giz.path.isEmpty()) {
        this.giz = null;
      } else {
        this.giz.path = new File(this.giz.path).getAbsolutePath();
      }
    }
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Allow fromString(String json) {
    return new Gson().fromJson(json, Allow.class);
  }
}
