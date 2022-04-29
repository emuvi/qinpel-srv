package br.net.pin.qinpel_srv.work;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.data.User;
import jakarta.servlet.http.HttpServletResponse;

public class OrdersAPPs {
  public static void send(File file, HttpServletResponse resp) throws IOException {
    resp.setContentType(Utils.getMimeType(file.getName()));
    resp.setContentLength((int) file.length());
    try (var input = new FileInputStream(file)) {
      IOUtils.copy(input, resp.getOutputStream());
    }
  }

  public static String list(Runny onWay, User forUser) {
    var appsDir = new File(onWay.air.setup.serverFolder, "app");
    if (forUser.master) {
      return Utils.listFolders(appsDir);
    }
    var result = new StringBuilder();
    for (var access : forUser.access) {
      if (access.app != null) {
        if (new File(appsDir, access.app.name).exists()) {
          result.append(access.app.name);
          result.append("\n");
        }
      }
    }
    return result.toString();
  }
}
