package br.net.pin.qinpel_srv.data;

import java.io.File;
import java.util.List;

import com.google.gson.Gson;

import br.net.pin.jabx.data.Registry;

public class Allow {
  public APP app;
  public DIR dir;
  public CMD cmd;
  public BAS bas;
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

  public class BAS {
    public String name;
    public Boolean mutate;
  }

  public class REG {
    public String base;
    public Registry registry;
    public Boolean all;
    public Boolean insert;
    public Boolean select;
    public Boolean update;
    public Boolean delete;
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
    if (this.bas != null) {
      if (this.bas.name == null || this.bas.name.isEmpty()) {
        this.bas = null;
      }
    }
    if (this.reg != null) {
      if (this.reg.base == null || this.reg.base.isEmpty() || this.reg.registry == null) {
        this.reg = null;
      } else {
        this.reg.all = this.reg.all != null ? this.reg.all : false;
        this.reg.insert = this.reg.insert != null ? this.reg.insert : false;
        this.reg.select = this.reg.select != null ? this.reg.select : false;
        this.reg.update = this.reg.update != null ? this.reg.update : false;
        this.reg.delete = this.reg.delete != null ? this.reg.delete : false;
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
