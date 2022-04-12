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

  public static User getUser(Runny onWay, HttpServletRequest req) {
    var token = getQinpelToken(req);
    if (token.isEmpty()) {
      return null;
    }
    return onWay.entry.getAuthed(token);
  }

  public static String getQinpelToken(HttpServletRequest req) {
    var token = req.getHeader("Qinpel-Token");
    if (token == null) {
      token = req.getParameter("Qinpel-Token");
    }
    if (token == null) {
      for (var cookie : req.getCookies()) {
        if (cookie.getName().equals("Qinpel-Token")) {
          token = cookie.getValue();
          break;
        }
      }
    }
    if (token == null) {
      token = "";
    }
    return token;
  }
}
