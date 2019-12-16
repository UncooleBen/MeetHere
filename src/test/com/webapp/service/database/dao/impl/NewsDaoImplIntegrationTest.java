package com.webapp.service.database.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class NewsDaoImplIntegrationTest {
    
    NewsDaoImpl newsDao;
    Connection connection;
    
    @BeforeEach
    void init() {
        this.newsDao = new NewsDaoImpl();
        String dbUrl = "jdbc:mysql://localhost:3306/meethere?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
        String dbUsername = "root";
        String dbPassword = "root";
        String dbClassname = "com.mysql.jdbc.Driver";
        ReflectionTestUtils.setField(this.newsDao, "dbUrl", dbUrl);
        ReflectionTestUtils.setField(this.newsDao, "dbUsername", dbUsername);
        ReflectionTestUtils.setField(this.newsDao, "dbPassword", dbPassword);
        ReflectionTestUtils.setField(this.newsDao, "dbClassname", dbClassname);
        this.connection = null;
    }

}