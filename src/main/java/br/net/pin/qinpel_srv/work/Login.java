package br.net.pin.qinpel_srv.work;

import java.util.Random;
import br.net.pin.qinpel_srv.data.Authed;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.swop.Logged;
import br.net.pin.qinpel_srv.swop.TryAuth;

public class Login {
  public static Logged tryEnter(TryAuth tryAuth, Runny onWay) {
    for (var user : onWay.air.users) {
      if (user.name.equals(tryAuth.name) && user.pass.equals(tryAuth.pass)) {
        var token = newToken();
        var authed = new Authed(user, System.currentTimeMillis());
        onWay.entry.putAuthed(token, authed);
        return new Logged(user.lang, token);
      }
    }
    return null;
  }

  private static String newToken() {
    var token = new StringBuilder();
    new Random().ints(36, 'a', 'z' + 1).forEach(ch -> {
      token.append((char) ch);
    });
    return token.toString();
  }
}
