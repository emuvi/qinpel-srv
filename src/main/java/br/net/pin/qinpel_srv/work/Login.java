package br.net.pin.qinpel_srv.work;

import br.net.pin.qinpel_srv.data.Authed;
import br.net.pin.qinpel_srv.data.Group;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.swap.Logged;
import br.net.pin.qinpel_srv.swap.TryAuth;
import jakarta.servlet.http.HttpServletRequest;

public class Login {
  public static Logged tryEnter(TryAuth tryAuth, Runny onWay, HttpServletRequest req) {
    for (var user : onWay.air.users) {
      if (user.name.equals(tryAuth.name) && user.pass.equals(tryAuth.pass)) {
        var token = req.getSession().getId();
        Group group = null;
        if (!user.group.isEmpty()) {
          for (var grouped : onWay.air.groups) {
            if (user.group.equals(grouped.name)) {
              group = grouped;
              break;
            }
          }
        }
        var authed = new Authed(user, group);
        onWay.autheds.addAuthed(token, authed);
        return new Logged(token, authed.getLang());
      }
    }
    return null;
  }
}
