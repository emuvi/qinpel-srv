package br.net.pin;

import java.io.File;
import java.nio.file.Files;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import br.net.pin.qinpel_srv.QinServer;
import br.net.pin.qinpel_srv.data.Bases;
import br.net.pin.qinpel_srv.data.Runny;
import br.net.pin.qinpel_srv.data.Setup;
import br.net.pin.qinpel_srv.data.Users;

public class QinpelSrv {

  public static void main(String[] args) throws Exception {
    var options = cmdOptions();
    var command = new DefaultParser().parse(options, args);
    if (command.hasOption('?')) {
      System.out.println(
          "QinpelSrv (Qinpel Server) is a library and a command program that servers public files, graphical user interfaces, file system access with authorization, command programs dispatchs, databases queries and scripts execution. It is the base of the Pointel platform and the backend of the Qinpel, the Quick Interface for Power Intelligence.");
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("qinpel-srv", options);
      return;
    }
    Setup setup;
    var setupFile = new File("setup.json");
    if (setupFile.exists()) {
      setup = Setup.fromString(Files.readString(setupFile.toPath()));
    } else {
      setup = new Setup();
    }
    setFromCmd(command, setup);
    Users users;
    var usersFile = new File("users.json");
    if (usersFile.exists()) {
      users = Users.fromString(Files.readString(usersFile.toPath()));
    } else {
      users = new Users();
    }
    Bases bases;
    var basesFile = new File("bases.json");
    if (basesFile.exists()) {
      bases = Bases.fromString(Files.readString(basesFile.toPath()));
    } else {
      bases = new Bases();
    }
    setup.fixDefaults();
    users.fixDefaults();
    bases.fixDefaults();
    new QinServer(new Runny(setup, users, bases)).start();
  }

  public static Options cmdOptions() {
    var result = new Options();
    result.addOption(Option.builder("?").longOpt("help").desc("Print usage information.")
        .build());
    result.addOption(Option.builder("v").longOpt("verbose").desc(
        "Should we print verbose messages?").build());
    result.addOption(Option.builder("k").longOpt("archive").desc(
        "Should we archive all the messages?").build());
    result.addOption(Option.builder("n").longOpt("name").hasArg().desc(
        "On behalf of what name should we serve?").build());
    result.addOption(Option.builder("h").longOpt("host").hasArg().desc(
        "On behalf of what name should we serve?").build());
    result.addOption(Option.builder("p").longOpt("port").hasArg().desc(
        "On what port should we serve?").build());
    result.addOption(Option.builder("u").longOpt("pubs").desc(
        "Should we serve public files?").build());
    result.addOption(Option.builder("a").longOpt("apps").desc(
        "Should we serve applications?").build());
    result.addOption(Option.builder("d").longOpt("dirs").desc(
        "Should we serve directories?").build());
    result.addOption(Option.builder("c").longOpt("cmds").desc("Should we serve commands?")
        .build());
    result.addOption(Option.builder("t").longOpt("dats").desc(
        "Should we serve databases access?").build());
    result.addOption(Option.builder("r").longOpt("regs").desc(
        "Should we serve register actions?").build());
    result.addOption(Option.builder("s").longOpt("sqls").desc(
        "Should we serve SQL executions?").build());
    result.addOption(Option.builder("l").longOpt("lizs").desc(
        "Should we serve LIZ executions?").build());
    return result;
  }

  public static void setFromCmd(CommandLine command, Setup setup) {
    if (command.hasOption('v')) {
      setup.serverVerbose = true;
    }
    if (command.hasOption('k')) {
      setup.serverArchive = true;
    }
    if (command.hasOption('n')) {
      setup.serverName = command.getOptionValue('n');
    }
    if (command.hasOption('h')) {
      setup.serverHost = command.getOptionValue('h');
    }
    if (command.hasOption('p')) {
      setup.serverPort = Integer.parseInt(command.getOptionValue('p'));
    }
    if (command.hasOption('u')) {
      setup.servesPUBs = true;
    }
    if (command.hasOption('a')) {
      setup.servesAPPs = true;
    }
    if (command.hasOption('d')) {
      setup.servesDIRs = true;
    }
    if (command.hasOption('c')) {
      setup.servesCMDs = true;
    }
    if (command.hasOption('t')) {
      setup.servesDATs = true;
    }
    if (command.hasOption('r')) {
      setup.servesREGs = true;
    }
    if (command.hasOption('s')) {
      setup.servesSQLs = true;
    }
    if (command.hasOption('l')) {
      setup.servesLIZs = true;
    }
  }

}
