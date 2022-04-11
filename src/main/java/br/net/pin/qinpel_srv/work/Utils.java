package br.net.pin.qinpel_srv.work;

import java.io.File;

public class Utils {
  public static String listFolders(File onDir) {
    var result = new StringBuilder();
    for (var file : onDir.listFiles()) {
      if (file.isDirectory()) {
        result.append(file.getName());
        result.append("\n");
      }
    }
    return result.toString();
  }

  public static String fixPath(String path) {
    return path.replace("\\", "/");
  }
}
