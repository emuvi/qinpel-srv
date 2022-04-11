package br.net.pin.qinpel_srv.work;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

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

  public static File newFile(String path, String parentIfRelative) {
    var result = new File(path);
    if (!result.isAbsolute()) {
      result = new File(parentIfRelative, path);
    }
    return result;
  }

  public static void close(Closeable resource) {
    if (resource != null) {
      try {
        resource.close();
      } catch (IOException ignore) {
      }
    }
  }
}
