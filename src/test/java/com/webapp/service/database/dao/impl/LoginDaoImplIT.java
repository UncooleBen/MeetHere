package com.webapp.service.database.dao.impl;

import com.webapp.model.user.Admin;
import com.webapp.model.user.Gender;
import com.webapp.model.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginDaoImplIT {

    LoginDaoImpl loginDao;
    UserDaoImpl userDao;
    Connection connection;

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
    }

    @AfterEach
    void tearDown() {
        this.loginDao.closeConnection(this.connection);
    }

    @Test
    void shouldReturnAdminWhenLoginWithAdmin() {
        User user = this.loginDao.login("root", "root");
        assertTrue(user instanceof Admin);
        assertEquals("root", user.getUsername());
        assertEquals("root", user.getPassword());
        assertEquals("pjt", user.getName());
        assertEquals(Gender.MALE, user.getSex());
        assertEquals(0, user.getId());
        assertEquals("999", user.getTel());
        assertEquals(0, user.getPermission());
    }

    @Test
    void shouldReturnUserWhenLoginWithUser() {
        User user = this.loginDao.login("pjt", "pjt");
        assertTrue(user instanceof User);
        assertEquals("pjt", user.getUsername());
        assertEquals("pjt", user.getPassword());
        assertEquals("john", user.getName());
        assertEquals(Gender.MALE, user.getSex());
        assertEquals(4, user.getId());
        assertEquals("123123123", user.getTel());
        assertEquals(1, user.getPermission());
    }

    @Test
    void shouldReturnUserWhenSignupANewUser() {
        User user = new User("username", "password", "name", "FEMALE", "1234");
        this.loginDao.signup(user);
        User signupUser = this.loginDao.login("username", "password");
        assertTrue(signupUser instanceof User);
        assertEquals(user.getUsername(), signupUser.getUsername());
        assertEquals(user.getPassword(), signupUser.getPassword());
        assertEquals(user.getName(), signupUser.getName());
        assertEquals(user.getSex(), signupUser.getSex());
        assertEquals(user.getTel(), signupUser.getTel());
        assertEquals(user.getPermission(), signupUser.getPermission());
        this.userDao.deleteUser(signupUser.getId());
    }

}