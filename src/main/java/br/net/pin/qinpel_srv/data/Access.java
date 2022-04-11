package br.net.pin.qinpel_srv.data;

import java.io.File;
import java.util.List;
import com.google.gson.Gson;
import br.net.pin.qinpel_srv.work.Utils;

public class Access {
  public APP app;
  public DIR dir;
  public CMD cmd;
  public DAT dat;
  public REG reg;
  public SQL sql;
  public LIZ liz;
  public GUZ guz;

  public class APP {
    public String name;
  }

  public class DIR {
    public String path;
    public Boolean mutable;
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
    public Boolean canWrite;
  }

  public class SQL {
    public String path;
  }

  public class LIZ {
    public String path;
  }

  public class GUZ {
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
        this.dir.path = Utils.fixPath(this.dir.path, new File("dir").getAbsolutePath());
        this.dir.mutable = this.dir.mutable != null ? this.dir.mutable : false;
      }
    }
    if (this.cmd != null) {
      if (this.cmd.name == null || this.cmd.name.isEmpty()) {
        this.cmd = null;
      }
    }
    if (this.dat != null) {
      if (this.dat.name == null || this.dat.name.isEmpty()) {
        this.dat = null;
      }
    }
    if (this.reg != null) {
      if (this.reg.name == null || this.reg.name.isEmpty()) {
        this.reg = null;
      } else {
        this.reg.canWrite = this.reg.canWrite != null ? this.reg.canWrite : false;
      }
    }
    if (this.sql != null) {
      if (this.sql.path == null || this.sql.path.isEmpty()) {
        this.sql = null;
      } else {
        this.sql.path = Utils.fixPath(this.sql.path, new File("sql").getAbsolutePath());
      }
    }
    if (this.liz != null) {
      if (this.liz.path == null || this.liz.path.isEmpty()) {
        this.liz = null;
      } else {
        this.liz.path = Utils.fixPath(this.liz.path, new File("liz").getAbsolutePath());
      }
    }
    if (this.guz != null) {
      if (this.guz.path == null || this.guz.path.isEmpty()) {
        this.guz = null;
      } else {
        this.guz.path = Utils.fixPath(this.guz.path, new File("guz").getAbsolutePath());
      }
    }
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static Access fromString(String json) {
    return new Gson().fromJson(json, Access.class);
  }
}
