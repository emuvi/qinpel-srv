package br.net.pin.qinpel_srv.work;

import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.data.User;

public class OrdersSTRs {
  public static String list(Runny onWay, User forUser) {
    var result = new StringBuilder();
    for (var base : onWay.air.bases) {
      if (forUser.master) {
        result.append(base.getName());
        result.append("\n");
      } else {
        for (var access : forUser.access) {
          if (access.str != null && access.str.name.equals(base.getName())) {
            result.append(base.getName());
            result.append("\n");
            break;
          }
        }
      }
    }
    return result.toString();
  }
}
