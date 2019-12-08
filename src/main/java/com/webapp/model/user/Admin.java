package com.webapp.model.user;

public class Admin extends User {

	private int permission = 0;

	public Admin(int id, String username, String password, String name, String sex, String tel) {
		super(id, username, password, name, sex, tel);
	}

}
