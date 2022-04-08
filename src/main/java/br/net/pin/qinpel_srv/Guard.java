package br.net.pin.qinpel_srv;

import jakarta.servlet.http.HttpServletRequest;

public class Guard {

  public static boolean allowAPP(String name, User forUser, SrvData ofSrv) {
    return true;
  }

  public static User getUser(SrvData ofSrv, HttpServletRequest onReq) {
    var token = getQinpelToken(onReq);
    return null;
  }
  
  public static String getQinpelToken(HttpServletRequest onReq) {
    var token = onReq.getHeader("Qinpel-Token");
    if (token == null) {
      token = onReq.getParameter("Qinpel-Token");
    }
    if (token == null) {
      for (var cookie : onReq.getCookies()) {
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
