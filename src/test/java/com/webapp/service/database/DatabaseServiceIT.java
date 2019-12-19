package com.webapp.service.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseServiceIT {

    DatabaseService databaseService;
    Connection connection;

    @BeforeEach
    void init() {
        String dbUrl = "jdbc:mysql://localhost:3306/meethere?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
        String dbUsername = "root";
        String dbPassword = "root";
        String dbClassname = "com.mysql.jdbc.Driver";
        this.databaseService = new DatabaseService();
        ReflectionTestUtils.setField(this.databaseService, "dbUrl", dbUrl);
        ReflectionTestUtils.setField(this.databaseService, "dbUsername", dbUsername);
        ReflectionTestUtils.setField(this.databaseService, "dbPassword", dbPassword);
        ReflectionTestUtils.setField(this.databaseService, "dbClassname", dbClassname);
        this.connection = null;
    }

    @AfterEach
    void tearDown() {
        this.databaseService.closeConnection(this.connection);
    }

    @Test
    void testGetConnection() throws Throwable {
        connection = databaseService.getConnection();
        assertAll(
                () -> assertNotNull(connection),
                () -> assertFalse(connection.isClosed())
        );
    }

    @Test
    void testCloseConnection() throws Throwable {
        connection = databaseService.getConnection();
        databaseService.closeConnection(connection);
        assertTrue(connection.isClosed());
    }

}