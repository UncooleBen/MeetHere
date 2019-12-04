package com.webapp.service.database.dao;

import com.webapp.model.user.Gender;
import com.webapp.model.user.User;

import java.util.List;

/**
 * This interface declares methods used to interact with table 't_user' in the database.
 *
 * @author Juntao Peng
 */
public interface UserDao {

    /**
     * Get all users from 't_user' of the database
     *
     * @return A list of users
     */
    List<User> queryAllUsers();

    /**
     * Query a user from 't_user' given user id.
     *
     * @param id The user id
     * @return A User if succeeded, otherwise null
     */
    User queryUserById(int id);

    /**
     * Query users who sign-up-ed with a given name.
     *
     * @param name The user's name
     * @return A list of user if there is users with the given name, otherwise an empty list
     */
    List<User> queryUserByName(String name);

    /**
     * Query users who sign-up-ed with a given username.
     *
     * @param username The username
     * @return A list user if there is users with the given username, otherwise null
     */
    User queryUserByUsername(String username);

    /**
     * Query users by sex
     *
     * @param gender The user's gender
     * @return A list of user if there is users with the given gender, otherwise an empty list
     */
    List<User> queryUserBySex(Gender gender);

    /**
     * Add a user into 't_user'
     *
     * @param user The given user
     * @return True if succeeded, otherwise false
     */
    boolean addUser(User user);

    /**
     * Delete a user from 't_user'
     *
     * @param id The user id
     * @return True if succeeded, otherwise false
     */
    boolean deleteUser(int id);

    /**
     * Update a user record in 't_user'
     * @param user The user to update
     * @return True if succeeded, otherwise false
     */
    boolean updateUser(User user);

    /**
     * Update a user's password in 't_user'
     * @param id The user's id
     * @param password The new password
     * @return True if succeeded, otherwise false
     */
    boolean updateUserPassword(int id, String password);
}
