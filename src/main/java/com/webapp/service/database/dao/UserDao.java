package com.webapp.service.database.dao;

import com.webapp.model.user.Gender;
import com.webapp.model.user.User;

import java.util.List;

/**
 * @author Juntao Peng
 */
public interface UserDao {

    public List<User> queryAllUsers();

    public User queryUserById(int id);

    public List<User> queryUserByName(String name);

    public List<User> queryUserBySex(Gender gender);

    public int addUser(User user);

    public boolean deleteUser(int id);

    public int updateUserAll(User user);

    public boolean updateUserPassword(int id, String password);
}
