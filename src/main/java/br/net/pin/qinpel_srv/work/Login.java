package br.net.pin.qinpel_srv.work;

import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.swap.Logged;
import br.net.pin.qinpel_srv.swap.TryAuth;
import jakarta.servlet.http.HttpServletRequest;

public class Login {
  public static Logged tryEnter(TryAuth tryAuth, Runny onWay, HttpServletRequest req) {
    for (var user : onWay.air.users) {
      if (user.name.equals(tryAuth.name) && user.pass.equals(tryAuth.pass)) {
        var token = req.getSession().getId();
        onWay.entry.putAuthed(token, user);
        return new Logged(user.lang, token);
      }
    }
    return null;
  }
}
