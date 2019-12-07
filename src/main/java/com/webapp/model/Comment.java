package com.webapp.model;

public class Comment {
  private int id;
  private int userId;
  private long date;
  private String content;

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

  public Comment() {}

  public Comment(int id, int userId, long date, String content) {
    this.id = id;
    this.userId = userId;
    this.date = date;
    this.content = content;
  }

  public Comment(int userId, long date, String content) {
    this.userId = userId;
    this.date = date;
    this.content = content;
  }
}
