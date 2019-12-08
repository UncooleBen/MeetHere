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
		super();
		this.id = id;
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
}
