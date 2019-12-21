package com.webapp.model;

/**
 * @author Juntao Peng
 */
public class News {
    private int id;
    private String title;
    private long created;
    private long lastModified;
    private String author;
    private String detail;

    public News(int id, String title, long created, long lastModified, String author, String detail) {
        this.id = id;
        this.title = title;
        this.created = created;
        this.lastModified = lastModified;
        this.author = author;
        this.detail = detail;
    }

    public News(String title, long created, long lastModified, String author, String detail) {
        this.title = title;
        this.created = created;
        this.lastModified = lastModified;
        this.author = author;
        this.detail = detail;
    }

    public News() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (id != news.id) return false;
        if (created != news.created) return false;
        if (lastModified != news.lastModified) return false;
        if (!title.equals(news.title)) return false;
        if (!author.equals(news.author)) return false;
        return detail.equals(news.detail);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + (int) (created ^ (created >>> 32));
        result = 31 * result + (int) (lastModified ^ (lastModified >>> 32));
        result = 31 * result + author.hashCode();
        result = 31 * result + detail.hashCode();
        return result;
    }
}
