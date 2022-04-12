package br.net.pin.qinpel_srv.work;

import java.io.File;
import java.util.Objects;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.data.User;
import jakarta.servlet.http.HttpServletRequest;

public class Guard {
  public static boolean allowAPP(String name, User forUser) {
    if (forUser.master) {
      return true;
    }
    for (var access : forUser.access) {
      if (access.app != null && Objects.equals(access.app.name, name)) {
        return true;
      }
    }
    return false;
  }

  public static boolean allowDIR(File path, User forUser, boolean toMutate) {
    if (forUser.master) {
      return true;
    }
    var absPath = path.getAbsolutePath();
    for (var access : forUser.access) {
      if (access.dir != null && absPath.startsWith(access.dir.path)) {
        if (toMutate) {
          if (access.dir.mutable) {
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
    return onWay.tokens.getAuthed(token);
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
