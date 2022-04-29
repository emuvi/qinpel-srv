package br.net.pin.qinpel_srv.data;

import java.util.concurrent.ConcurrentHashMap;

public class Entry extends ConcurrentHashMap<String, User> {
  public User getAuthed(String token) {
    return this.get(token);
  }

  public void putAuthed(String token, User user) {
    this.put(token, user);
  }

  public void delAuthed(String token) {
    this.remove(token);
  }
}
