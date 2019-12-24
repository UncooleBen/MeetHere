package com.webapp.service.database.dao.impl;

import com.webapp.model.user.Admin;
import com.webapp.model.user.Gender;
import com.webapp.model.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Juntao Peng
 */
class LoginDaoImplIT {

    LoginDaoImpl loginDao;
    UserDaoImpl userDao;
    Connection connection;
    User addedUser;
    Admin addedAdmin;

    @BeforeEach
    void init() {
        this.loginDao = new LoginDaoImpl();
        String dbUrl = "jdbc:mysql://localhost:3306/meethere?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
        String dbUsername = "root";
        String dbPassword = "root";
        String dbClassname = "com.mysql.jdbc.Driver";
        ReflectionTestUtils.setField(this.loginDao, "dbUrl", dbUrl);
        ReflectionTestUtils.setField(this.loginDao, "dbUsername", dbUsername);
        ReflectionTestUtils.setField(this.loginDao, "dbPassword", dbPassword);
        ReflectionTestUtils.setField(this.loginDao, "dbClassname", dbClassname);
        this.userDao = new UserDaoImpl();
        ReflectionTestUtils.setField(this.userDao, "dbUrl", dbUrl);
        ReflectionTestUtils.setField(this.userDao, "dbUsername", dbUsername);
        ReflectionTestUtils.setField(this.userDao, "dbPassword", dbPassword);
        ReflectionTestUtils.setField(this.userDao, "dbClassname", dbClassname);
        this.connection = null;
        this.addedUser = new User("username1", "password1", "name1", "MALE", "tel1");
        this.addedAdmin = new Admin("admin1", "password1", "name1", "MALE", "tel1");
        this.userDao.addUser(addedUser);
        this.userDao.addUser(addedAdmin);
        List<User> users = this.userDao.queryAllUsers();
        for (User user : users) {
            if (user.getPermission() == 0) {
                this.addedAdmin = (Admin) user;
            } else {
                this.addedUser = user;
            }
        }
    }

    @AfterEach
    void tearDown() {
        this.loginDao.closeConnection(this.connection);
        this.userDao.deleteUser(addedUser.getId());
        this.userDao.deleteUser(addedAdmin.getId());
    }

    @Test
    void shouldReturnAdminWhenLoginWithAdmin() {
        User user = this.loginDao.login("admin1", "password1");
        assertAll(
                () -> assertTrue(user instanceof Admin),
                () -> assertEquals("admin1", user.getUsername()),
                () -> assertEquals("password1", user.getPassword()),
                () -> assertEquals("name1", user.getName()),
                () -> assertEquals(Gender.MALE, user.getSex()),
                () -> assertEquals(addedAdmin.getId(), user.getId()),
                () -> assertEquals("tel1", user.getTel()),
                () -> assertEquals(0, user.getPermission()));
    }

    @Test
    void shouldReturnUserWhenLoginWithUser() {
        User user = this.loginDao.login("username1", "password1");
        assertAll(
                () -> assertEquals("username1", user.getUsername()),
                () -> assertEquals("password1", user.getPassword()),
                () -> assertEquals("name1", user.getName()),
                () -> assertEquals(Gender.MALE, user.getSex()),
                () -> assertEquals(addedUser.getId(), user.getId()),
                () -> assertEquals("tel1", user.getTel()),
                () -> assertEquals(1, user.getPermission()));
    }

    @Test
    void shouldReturnUserWhenSignupANewUser() {
        User user = new User("username", "password", "name", "FEMALE", "1234");
        this.loginDao.signup(user);
        User signupUser = this.loginDao.login("username", "password");
        assertAll(
                () -> assertEquals(user.getUsername(), signupUser.getUsername()),
                () -> assertEquals(user.getPassword(), signupUser.getPassword()),
                () -> assertEquals(user.getName(), signupUser.getName()),
                () -> assertEquals(user.getSex(), signupUser.getSex()),
                () -> assertEquals(user.getTel(), signupUser.getTel()),
                () -> assertEquals(user.getPermission(), signupUser.getPermission()));
        this.userDao.deleteUser(signupUser.getId());
    }

}