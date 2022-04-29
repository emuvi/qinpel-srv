package br.net.pin.qinpel_srv.work;

import java.io.File;

import br.net.pin.qinpel_srv.data.Authed;
import br.net.pin.qinpel_srv.data.Runny;
import jakarta.servlet.http.HttpServletRequest;

public class Guard {
  public static boolean allowAPP(String name, Authed forAuthed) {
    return forAuthed.allowAPP(name);
  }

  public static boolean allowDIR(File path, Authed forAuthed, boolean toMutate) {
    return forAuthed.allowDIR(path.getAbsolutePath(), toMutate);
  }

  public static Authed getAuthed(Runny onWay, HttpServletRequest req) {
    return onWay.autheds.getAuthed(Guard.getToken(req));
  }

  public static String getToken(HttpServletRequest req) {
    return req.getSession().getId();
  }
}
