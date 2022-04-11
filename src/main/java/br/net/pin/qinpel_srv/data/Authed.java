package br.net.pin.qinpel_srv.data;

public class Authed {
    public User user;
    public Long from;

    public Authed(User user, Long from) {
        this.user = user;
        this.from = from;
    }
}
