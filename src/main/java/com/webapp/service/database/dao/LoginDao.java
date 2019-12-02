package com.webapp.service.database.dao;

import com.webapp.model.user.User;

public interface LoginDao {

    public User login(String loginUsername, String loginPassword);

    public User signup(User user);
}
