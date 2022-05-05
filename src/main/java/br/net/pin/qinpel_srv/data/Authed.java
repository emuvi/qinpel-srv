package br.net.pin.qinpel_srv.data;

import br.net.pin.jabx.data.Deed;
import br.net.pin.jabx.data.Registry;

public class Authed {
  public final User user;
  public final Group group;

  public Authed(User user, Group group) {
    this.user = user;
    this.group = group;
  }

  public String getHome() {
    if (!this.user.home.isEmpty()) {
      return this.user.home;
    } else if (this.group != null) {
      return this.group.home;
    } else {
      return "";
    }
  }

  public String getLang() {
    if (!this.user.lang.isEmpty()) {
      return this.user.lang;
    } else if (this.group != null) {
      return this.group.lang;
    } else {
      return "";
    }
  }

  public Boolean isMaster() {
    if (this.user.master) {
      return true;
    } else if (this.group != null) {
      return this.group.master;
    } else {
      return false;
    }
  }

  public boolean allowAPP(String name) {
    if (isMaster()) {
      return true;
    }
    for (var access : this.user.access) {
      if (access.app != null && access.app.name.equals(name)) {
        return true;
      }
    }
    if (this.group != null) {
      for (var access : this.group.access) {
        if (access.app != null && access.app.name.equals(name)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean allowDIR(String fullPath, boolean toMutate) {
    if (this.isMaster()) {
      return true;
    }
    for (var access : this.user.access) {
      if (access.dir != null && fullPath.startsWith(access.dir.path)) {
        if (toMutate) {
          if (access.dir.mutate) {
            return true;
          }
        } else {
          return true;
        }
      }
    }
    if (this.group != null) {
      for (var access : this.group.access) {
        if (access.dir != null && fullPath.startsWith(access.dir.path)) {
          if (toMutate) {
            if (access.dir.mutate) {
              return true;
            }
          } else {
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean allowCMD(String name) {
    if (isMaster()) {
      return true;
    }
    for (var access : this.user.access) {
      if (access.cmd != null && access.cmd.name.equals(name)) {
        return true;
      }
    }
    if (this.group != null) {
      for (var access : this.group.access) {
        if (access.cmd != null && access.cmd.name.equals(name)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean allowBAS(String name, boolean toMutate) {
    if (this.isMaster()) {
      return true;
    }
    for (var access : this.user.access) {
      if (access.bas != null && access.bas.name.equals(name)) {
        if (toMutate) {
          if (access.bas.mutate) {
            return true;
          }
        } else {
          return true;
        }
      }
    }
    if (this.group != null) {
      for (var access : this.group.access) {
        if (access.bas != null && access.bas.name.equals(name)) {
          if (toMutate) {
            if (access.bas.mutate) {
              return true;
            }
          } else {
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean allowREG(Registry registry, Deed deed) {
    if (this.isMaster()) {
      return true;
    }
    if (!this.allowBAS(registry.base, deed.mutates)) {
      return false;
    }
    for (var access : this.user.access) {
      if (access.reg != null && access.reg.registry.equals(registry)) {
        if (access.reg.all) {
          return true;
        }
        switch (deed) {
          case INSERT:
            if (access.reg.insert) {
              return true;
            }
            break;
          case SELECT:
            if (access.reg.select) {
              return true;
            }
            break;
          case UPDATE:
            if (access.reg.update) {
              return true;
            }
            break;
          case DELETE:
            if (access.reg.delete) {
              return true;
            }
            break;
        }
      }
    }
    if (this.group != null) {
      for (var access : this.group.access) {
        if (access.reg != null && access.reg.registry.equals(registry)) {
          if (access.reg.all) {
            return true;
          }
          switch (deed) {
            case INSERT:
              if (access.reg.insert) {
                return true;
              }
              break;
            case SELECT:
              if (access.reg.select) {
                return true;
              }
              break;
            case UPDATE:
              if (access.reg.update) {
                return true;
              }
              break;
            case DELETE:
              if (access.reg.delete) {
                return true;
              }
              break;
          }
        }
      }
    }
    return false;
  }
}
