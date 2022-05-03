package br.net.pin.qinpel_srv.work;

import java.io.StringWriter;

import br.net.pin.jabx.data.Delete;
import br.net.pin.jabx.data.Insert;
import br.net.pin.jabx.data.Select;
import br.net.pin.jabx.data.Update;
import br.net.pin.jabx.flow.CSVMaker;
import br.net.pin.jabx.flow.CSVWrite;
import br.net.pin.jabx.mage.WizData;
import br.net.pin.qinpel_srv.data.Way;
import jakarta.servlet.ServletException;

public class OrdersREG {
  public static String regNew(Way way, String base, Insert insert) throws ServletException {
    try {
      System.out.println(insert.toString());
      var helped = way.stores.getHelp(base);
      var result = helped.helper.insert(helped.link, insert);
      return "Affected: " + result;
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public static String regAsk(Way way, String base, Select select) throws ServletException {
    try {
      var helped = way.stores.getHelp(base);
      var result = helped.helper.select(helped.link, select);
      var maker = new CSVMaker(result, select.fields);
      var build = new StringWriter();
      try (var write = new CSVWrite(build)) {
        String[] line = WizData.getColumnNames(result);
        write.writeLine(line);
        while ((line = maker.makeLine()) != null) {
          write.writeLine(line);
        }
      }
      return build.toString();
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public static String regSet(Way way, String base, Update update) throws ServletException {
    try {
      var helped = way.stores.getHelp(base);
      var result = helped.helper.update(helped.link, update);
      return "Affected: " + result;
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public static String regDel(Way way, String base, Delete delete) throws ServletException {
    try {
      var helped = way.stores.getHelp(base);
      var result = helped.helper.delete(helped.link, delete);
      return "Affected: " + result;
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
