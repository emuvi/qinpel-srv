package br.net.pin.qinpel_srv.work;

import java.io.File;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.data.User;

public class Lists {

  public static String listApps(Runny onWay, User forUser) {
    var appsDir = new File(onWay.setup.serverFolder, "app");
    if (forUser.master) {
      return listFolders(appsDir);
    }
    var result = new StringBuilder();
    for (var access : forUser.access) {
      if (access.app != null && access.app.name != null && !access.app.name.isEmpty()) {
        if (new File(appsDir, access.app.name).exists()) {
          result.append(access.app.name);
          result.append("\n");
        }
      }
    }
    return result.toString();
  }

  private static String listFolders(File onDir) {
    var result = new StringBuilder();
    for (var file : onDir.listFiles()) {
      if (file.isDirectory()) {
        result.append(file.getName());
        result.append("\n");
      }
    }
    return result.toString();
  }

}
