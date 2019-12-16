package com.webapp.service.database.dao.impl;


import com.webapp.model.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplIntegrationTest {
    UserDaoImpl userDao;
    Connection connection;

    @BeforeEach
    void init() {
        this.userDao = new UserDaoImpl();
        String dbUrl = "jdbc:mysql://localhost:3306/meethere?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
        String dbUsername = "root";
        String dbPassword = "root";
        String dbClassname = "com.mysql.jdbc.Driver";
        ReflectionTestUtils.setField(this.userDao, "dbUrl", dbUrl);
        ReflectionTestUtils.setField(this.userDao, "dbUsername", dbUsername);
        ReflectionTestUtils.setField(this.userDao, "dbPassword", dbPassword);
        ReflectionTestUtils.setField(this.userDao, "dbClassname", dbClassname);
        this.connection = null;
    }

    @AfterEach
    void tearDown() {
        this.userDao.closeConnection(this.connection);
    }

    @Test
    void addAUserAndQueryItByList() {
        long time = System.currentTimeMillis();
        User user = new User("username", "password", "name", "MALE", "tel");
        this.userDao.addUser(user);
        User insertedUser = this.userDao.queryAllUsers().get(0);
        assertEquals(user.getPermission(), insertedUser.getPermission());
        assertEquals(user.getTel(), insertedUser.getTel());
        assertEquals(user.getSex(), insertedUser.getSex());
        assertEquals(user.getName(), insertedUser.getName());
        assertEquals(user.getUsername(), insertedUser.getUsername());
        assertEquals(user.getPassword(), insertedUser.getPassword());
        this.userDao.deleteUser(insertedUser.getId());
    }

    @Test
    void addAUserAndQueryItById() {
        long time = System.currentTimeMillis();
        User user = new User("username", "password", "name", "MALE", "tel");
        this.userDao.addUser(user);
        User insertedUser = this.userDao.queryUserById(this.userDao.queryAllUsers().get(0).getId());
        assertEquals(user.getPermission(), insertedUser.getPermission());
        assertEquals(user.getTel(), insertedUser.getTel());
        assertEquals(user.getSex(), insertedUser.getSex());
        assertEquals(user.getName(), insertedUser.getName());
        assertEquals(user.getUsername(), insertedUser.getUsername());
        assertEquals(user.getPassword(), insertedUser.getPassword());
        this.userDao.deleteUser(insertedUser.getId());
    }


    @Test
    void add20UserAndQueryByList() {
        List<User> userList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i=0; i<20; i++) {
            userList.add(new User("username"+i, "password"+i, "name"+i,
                    "MALE", "tel"+i));
        }
        for (User user : userList) {
            this.userDao.addUser(user);
        }
        List<User> resultList = this.userDao.queryAllUsers();
        for (int i=0; i<20; i++) {
            User user = userList.get(20-i-1);
            User insertedUser = resultList.get(i);
            assertEquals(user.getPermission(), insertedUser.getPermission());
            assertEquals(user.getTel(), insertedUser.getTel());
            assertEquals(user.getSex(), insertedUser.getSex());
            assertEquals(user.getName(), insertedUser.getName());
            assertEquals(user.getUsername(), insertedUser.getUsername());
            assertEquals(user.getPassword(), insertedUser.getPassword());
        }
        for (int i=0; i<20; i++) {
            User user = resultList.get(i);
            this.userDao.deleteUser(user.getId());
        }
    }

    @Test
    void add20UserAndUpdate() {
        List<User> userList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i=0; i<20; i++) {
            userList.add(new User("username"+i, "password"+i, "name"+i,
                    "MALE", "tel"+i));
        }
        for (User user : userList) {
            this.userDao.addUser(user);
        }
        List<User> resultList = this.userDao.queryAllUsers();
        for (int i=0; i<20; i++) {
            User resultUser = resultList.get(0);
            resultUser.setName("new name");
            resultUser.setPassword("new password");
            resultUser.setUsername("new username");
            resultUser.setSex("FEMALE");
            resultUser.setTel("new tel");
            this.userDao.updateUser(resultUser);
        }
        List<User> updatedResultList = this.userDao.queryAllUsers();
        for (int i=0; i<20; i++) {
            User user = resultList.get(i);
            User insertedUser = updatedResultList.get(i);
            assertEquals(user.getPermission(), insertedUser.getPermission());
            assertEquals(user.getTel(), insertedUser.getTel());
            assertEquals(user.getSex(), insertedUser.getSex());
            assertEquals(user.getName(), insertedUser.getName());
            assertEquals(user.getUsername(), insertedUser.getUsername());
            assertEquals(user.getPassword(), insertedUser.getPassword());
        }
        for (int i=0; i<20; i++) {
            User user = resultList.get(i);
            this.userDao.deleteUser(user.getId());
        }
    }

    @Test
    void add20UserAndUpdatePassword() {
        List<User> userList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i=0; i<20; i++) {
            userList.add(new User("username"+i, "password"+i, "name"+i,
                    "MALE", "tel"+i));
        }
        for (User user : userList) {
            this.userDao.addUser(user);
        }
        List<User> resultList = this.userDao.queryAllUsers();
        for (int i=0; i<20; i++) {
            User resultUser = resultList.get(i);
            this.userDao.updateUserPassword(resultUser.getId(), "new password"+i);
        }
        List<User> updatedResultList = this.userDao.queryAllUsers();
        for (int i=0; i<20; i++) {
            User user = resultList.get(i);
            User insertedUser = updatedResultList.get(i);
            assertEquals(user.getPermission(), insertedUser.getPermission());
            assertEquals(user.getTel(), insertedUser.getTel());
            assertEquals(user.getSex(), insertedUser.getSex());
            assertEquals(user.getName(), insertedUser.getName());
            assertEquals(user.getUsername(), insertedUser.getUsername());
            assertEquals("new password"+i, insertedUser.getPassword());
        }
        for (int i=0; i<20; i++) {
            User user = resultList.get(i);
            this.userDao.deleteUser(user.getId());
        }
    }
}