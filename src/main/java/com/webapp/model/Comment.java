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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != comment.id) return false;
        if (userId != comment.userId) return false;
        if (date != comment.date) return false;
        if (verified != comment.verified) return false;
        return content.equals(comment.content);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + content.hashCode();
        result = 31 * result + (verified ? 1 : 0);
        return result;
    }
}
