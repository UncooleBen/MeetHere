package com.webapp.model;

public class News {
	private int id;
	private String title;
	private long created;
	private long last_modified;
	private String author;
	private String detail;

	public News(int id, String title, long created, String author, String detail) {
		super();
		this.id = id;
		this.title = title;
		this.created = created;
		this.author = author;
		this.detail = detail;
	}
}
