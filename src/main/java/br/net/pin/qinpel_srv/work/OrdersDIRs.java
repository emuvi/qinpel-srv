package br.net.pin.qinpel_srv.work;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class OrdersDIRs {
  public static String dirList(File path) {
    var result = new StringBuilder();
    result.append("P: ");
    result.append(path.getAbsolutePath());
    result.append("\n");
    for (var inside : path.listFiles()) {
      result.append(inside.isDirectory() ? "D: " : "F: ");
      result.append(inside.getName());
      result.append("\n");
    }
    return result.toString();
  }

  public static String dirNew(File path) throws IOException {
    Files.createDirectories(path.toPath());
    return "Folder created: " + path.getAbsolutePath();
  }
}
