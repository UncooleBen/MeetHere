package com.webapp.model;

public class Comment {
  private int id;
  private int userId;
  private long date;
  private String content;
  private boolean verified;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Comment(int id, int userId, long date, String content, boolean verified) {
    this.id = id;
    this.userId = userId;
    this.date = date;
    this.content = content;
    this.verified = verified;
  }

  public boolean isVerified() {
    return verified;
  }

  public Comment() {
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public Comment(int userId, long date, String content) {
    this.userId = userId;
    this.date = date;
    this.content = content;
  }
}
