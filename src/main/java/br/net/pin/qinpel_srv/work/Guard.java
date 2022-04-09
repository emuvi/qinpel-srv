package br.net.pin.qinpel_srv.work;

import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.data.User;
import jakarta.servlet.http.HttpServletRequest;

public class Guard {

  public static boolean allowAPP(String name, User forUser, Runny onWay) {
    return true;
  }

  public static User getUser(Runny onWay, HttpServletRequest req) {
    var token = getQinpelToken(req);
    if (token.isEmpty()) {
      return null;
    }
    var authed = onWay.tokens.get(token);
    if (authed == null) {
      return null;
    }
    return authed.user;
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
