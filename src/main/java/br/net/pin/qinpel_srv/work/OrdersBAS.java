package br.net.pin.qinpel_srv.work;

import br.net.pin.qinpel_srv.data.Authed;
import br.net.pin.qinpel_srv.data.Runny;

public class OrdersBAS {
  public static String list(Runny onWay, Authed forAuthed) {
    var result = new StringBuilder();
    for (var base : onWay.air.bases) {
      var name = base.getName();
      if (forAuthed.allowBAS(name, false)) {
        result.append(name).append("\n");
      }
    }
    return result.toString();
  }
}
