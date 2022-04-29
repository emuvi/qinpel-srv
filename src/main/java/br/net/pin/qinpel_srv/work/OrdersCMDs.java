package br.net.pin.qinpel_srv.work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.data.User;
import br.net.pin.qinpel_srv.swap.Execute;

public class OrdersCMDs {
  public static String run(File executable, Execute execution) throws Exception {
    var builder = new ProcessBuilder();
    var buffer = new ArrayList<String>();
    if (executable.getName().toLowerCase().endsWith(".jar")) {
      buffer.add("java");
      buffer.add("-jar");
      buffer.add(executable.getAbsolutePath());
    } else {
      buffer.add(executable.getAbsolutePath());
    }
    if (execution.args != null) {
      buffer.addAll(execution.args);
    }
    builder.command(buffer);
    builder.redirectErrorStream(true);
    var process = builder.start();
    Thread inputsThread = null;
    var inputsException = execution.inputs != null ? new AtomicReference<Exception>(null)
        : null;
    if (execution.inputs != null) {
      inputsThread = new Thread() {
        @Override
        public void run() {
          try {
            var writer = new BufferedWriter(new OutputStreamWriter(process
                .getOutputStream()));
            for (var input : execution.inputs) {
              writer.write(input);
              writer.newLine();
              writer.flush();
            }
          } catch (Exception e) {
            inputsException.set(e);
          }
        };
      };
      inputsThread.start();
    }
    buffer.clear();
    Exception outputsException = null;
    var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        buffer.add(line);
      }
    } catch (Exception e) {
      outputsException = e;
    }
    if (inputsThread != null) {
      inputsThread.join();
    }
    if (inputsException != null && inputsException.get() != null) {
      throw new Exception("Error on send the inputs to the command.", inputsException
          .get());
    }
    if (outputsException != null) {
      throw new Exception("Error on getting the outputs from the command.",
          outputsException);
    }
    var exitCode = process.waitFor();
    var result = new StringBuilder();
    result.append(exitCode);
    result.append("\n'");
    for (var line : buffer) {
      result.append(line);
      result.append("\n");
    }
    return result.toString();
  }

  public static String list(Runny onWay, User forUser) {
    var cmdsDir = new File(onWay.air.setup.serverFolder, "cmd");
    if (forUser.master) {
      return Utils.listFolders(cmdsDir);
    }
    var result = new StringBuilder();
    for (var access : forUser.access) {
      if (access.cmd != null) {
        if (new File(cmdsDir, access.cmd.name).exists()) {
          result.append(access.cmd.name);
          result.append("\n");
        }
      }
    }
    return result.toString();
  }
}
