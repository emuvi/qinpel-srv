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

  public static boolean isAbsolute(String path) {
    var sep = path.contains("\\") ? "\\" : "/";
    if (path.startsWith(sep)) {
      return true;
    }
    var firstSlash = path.indexOf(sep);
    if (firstSlash > -1) {
      var firstSegment = path.substring(0, firstSlash);
      if (firstSegment.contains(":")) {
        return true;
      }
    }
    return false;
  }

  public static String fixPath(String path, String parent) {
    path = path.replace("\\", "/");
    if (!isAbsolute(path)) {
      parent = parent.replace("\\", "/");
      return parent + (parent.endsWith("/") ? "" : "/") + path;
    } else {
      return path.replace("\\", "/"); // [ TODO ] - Use Pathed class
    }
  }
}
