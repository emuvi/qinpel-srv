package br.net.pin.qinpel_srv.data;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.dbcp2.BasicDataSource;

public class Store {
  private final Air air;
  private final Map<String, BasicDataSource> links;

  public Store(Air air) throws Exception {
    this.air = air;
    if (this.air.setup.servesSTRs) {
      this.links = new ConcurrentHashMap<>();
      for (var base : this.air.bases) {
        var bds = new BasicDataSource();
        bds.setUrl(base.jdbc);        
        if (!base.user.isEmpty()) {
          bds.setUsername(base.user);
        }
        if (!base.pass.isEmpty()) {
          bds.setPassword(base.pass);
        }
        bds.setMinIdle(this.air.setup.storeMinIdle);
        bds.setMaxIdle(this.air.setup.storeMaxIdle);
        bds.setMaxTotal(this.air.setup.storeMaxTotal);
        this.links.put(base.name, bds);
      }
    } else {
      this.links = null;
    }
  }

  public Connection getLink(String base) throws Exception {
    if (this.links == null) {
      return null;
    }
    var bds = this.links.get(base);
    if (bds == null) {
      return null;
    }
    return bds.getConnection();
  }
}
