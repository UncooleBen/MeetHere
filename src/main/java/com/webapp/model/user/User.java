package com.webapp.model.user;

public class User {
	private int id;
	private String username;
	private String password;
	private String name;
	private Gender sex;
	private int permission;
	private String tel;

	public User() {

	}

	public User(String username, String password, String name, String sex, String tel) {
		super();
		this.id = 0;
		this.username = username;
		this.password = password;
		this.name = name;
		this.sex = Gender.valueOf(sex);
		this.permission = 1;
		this.tel = tel;
	}

	public User(int id, String username, String password, String name, String sex, String tel) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.sex = Gender.valueOf(sex);
		this.permission = 1;
		this.tel = tel;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sex
	 */
	public Gender getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = Gender.valueOf(sex);
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the permission
	 */
	public int getPermission() {
		return permission;
	}

}
