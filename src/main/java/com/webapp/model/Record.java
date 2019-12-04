package com.webapp.model;

/** @author Juntao Peng */
public class Record {

  private int id;
  private long time;
  private long startDate;
  private long endDate;
  private int userId;
  private int buildId;
  private boolean verified;

  public Record(
      int id, long time, long startDate, long endDate, int userId, int buildId, boolean verified) {
    this.id = id;
    this.time = time;
    this.startDate = startDate;
    this.endDate = endDate;
    this.userId = userId;
    this.buildId = buildId;
    this.verified = verified;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public long getStartDate() {
    return startDate;
  }

  public void setStartDate(long startDate) {
    this.startDate = startDate;
  }

  public long getEndDate() {
    return endDate;
  }

  public void setEndDate(long endDate) {
    this.endDate = endDate;
  }

  public int getUserId() {
    return userId;
  }

  public Record() {}

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getBuildId() {
    return buildId;
  }

  public void setBuildId(int buildId) {
    this.buildId = buildId;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }
}
