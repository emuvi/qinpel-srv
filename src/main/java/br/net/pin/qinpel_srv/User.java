package br.net.pin.qinpel_srv;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

  public String name;
  public String pass;
  public String home;
  public String lang;
  public Boolean master;
  public List<Access> access;

}
