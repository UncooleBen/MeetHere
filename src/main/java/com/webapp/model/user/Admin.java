package com.webapp.model.user;

public class Admin extends User {

    public Admin(int id, String username, String password, String name, String sex, String tel) {
        super(id, username, password, name, sex, tel, 0);
    }

    public Admin(String username, String password, String name, String sex, String tel) {
        super(username, password, name, sex, 0, tel);

    }

    public Admin() {

    }

}
