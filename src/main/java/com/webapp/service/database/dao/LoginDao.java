package com.webapp.service.database.dao;

import com.webapp.model.user.User;

/**
 * This interface declares methods used to interact with table 't_user' in the database for login and signup
 * purpose.
 *
 * @author Juntao Peng
 */
public interface LoginDao {

    /**
     * Login a user into the website given his username and password.
     *
     * @param loginUsername A string of username
     * @param loginPassword A string of password
     * @return The User if both are correct, otherwise null
     */
    User login(String loginUsername, String loginPassword);

    /**
     * Sign-up a user into 't_user' in database
     *
     * @param user The user needed to be signed-up
     * @return The User if sign-up succeeded, otherwise null
     */
    User signup(User user);
}
