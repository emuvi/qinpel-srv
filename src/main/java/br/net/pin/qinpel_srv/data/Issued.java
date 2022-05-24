package br.net.pin.qinpel_srv.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Issued {

  private final List<String> outs;
  private final List<String> errs;
  private volatile Integer exitCode;

  private final ReadWriteLock outsLock;
  private final ReadWriteLock errsLock;
  private final ReadWriteLock exitLock;

  public Issued() {
    this.outs = new ArrayList<>();
    this.errs = new ArrayList<>();
    this.exitCode = null;
    this.outsLock = new ReentrantReadWriteLock();
    this.errsLock = new ReentrantReadWriteLock();
    this.exitLock = new ReentrantReadWriteLock();
  }

  public void addOut(String out) {
    this.outsLock.writeLock().lock();
    try {
      this.outs.add(out);
    } finally {
      this.outsLock.writeLock().unlock();
    }
  }

  public String getOuts() {
    this.outsLock.readLock().lock();
    try {
      return String.join("\n", this.outs);
    } finally {
      this.outsLock.readLock().unlock();
    }
  }

  public void addErr(String err) {
    this.errsLock.writeLock().lock();
    try {
      this.errs.add(err);
    } finally {
      this.errsLock.writeLock().unlock();
    }
  }

  public String getErrs() {
    this.errsLock.readLock().lock();
    try {
      return String.join("\n", this.errs);
    } finally {
      this.errsLock.readLock().unlock();
    }
  }

  public Integer getExitCode() {
    this.exitLock.readLock().lock();
    try {
      return this.exitCode;
    } finally {
      this.exitLock.readLock().unlock();
    }
  }

  public void setExitCode(Integer exitCode) {
    this.exitLock.writeLock().lock();
    try {
      this.exitCode = exitCode;
    } finally {
      this.exitLock.writeLock().unlock();
    }
  }

  public boolean hasFinished() {
    this.exitLock.readLock().lock();
    try {
      return this.exitCode != null;
    } finally {
      this.exitLock.readLock().unlock();
    }
  }
}
