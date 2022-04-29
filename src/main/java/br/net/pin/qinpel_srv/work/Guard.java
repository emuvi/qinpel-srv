package br.net.pin.qinpel_srv.work;

import java.io.File;

import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.data.User;
import jakarta.servlet.http.HttpServletRequest;

public class Guard {
  public static boolean allowAPP(String name, User forUser) {
    if (forUser.master) {
      return true;
    }
    for (var access : forUser.access) {
      if (access.app != null && access.app.name.equals(name)) {
        return true;
      }
    }
    return false;
  }

  public static boolean allowDIR(File path, User forUser, boolean toMutate) {
    if (forUser.master) {
      return true;
    }
    var fullPath = path.getAbsolutePath();
    for (var access : forUser.access) {
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
    return false;
  }

  public static User getAuthed(Runny onWay, HttpServletRequest req) {
    return onWay.entry.getAuthed(Guard.getToken(req));
  }

  public static String getToken(HttpServletRequest req) {
    return req.getSession().getId();
  }
}
