package com.webapp.service.database.dao.impl;


import com.webapp.model.user.Admin;
import com.webapp.model.user.Gender;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDaoImplTest {
    private UserDao userDao;
    private Connection connection = mock(Connection.class);
    private PreparedStatement preparedStatement = mock(PreparedStatement.class);
    private SQLException test_sql_exception;
    private ResultSet rs = mock(ResultSet.class);
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;


    class TestableUserDaoImpl extends UserDaoImpl {
        @Override
        public Connection getConnection() {
            return connection;
        }
    }

    @BeforeEach
    void init() {
        this.userDao = new TestableUserDaoImpl();
        this.test_sql_exception = new SQLException();
        this.outContent = new ByteArrayOutputStream();
        this.errContent = new ByteArrayOutputStream();
        this.originalOut = System.out;
        this.originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tear_down() throws IOException {
        System.setErr(this.originalErr);
        System.setOut(this.originalOut);
        this.outContent.close();
        this.errContent.close();
    }


    @Test
    void test_throws_sql_exception_when_query_All_Users() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.userDao.queryAllUsers();
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_queryAllUsers_WhenPermissionIs_0() throws SQLException {
        int id = 305;
        String username = "PG";
        String password = "123466";
        String name = "pengge";
        String sex = "FEMALE";
        int permission = 0;
        String tel = "911";

        Admin admin = new Admin(id, username, password, name, sex, tel);

        List<User> userList = new ArrayList<>();
        userList.add(admin);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */

        when(rs.getInt("id")).thenReturn(id);
        when(rs.getString("username")).thenReturn(username);
        when(rs.getString("password")).thenReturn(password);
        when(rs.getString("name")).thenReturn(name);
        when(rs.getString("sex")).thenReturn(sex);
        when(rs.getString("tel")).thenReturn(tel);
        when(rs.getInt("permission")).thenReturn(permission);


        assertEquals(userList, this.userDao.queryAllUsers());

    }

    @Test
    void test_queryAllUsers_WhenPermissionIs_1() throws SQLException {
        int id = 305;
        String username = "PG";
        String password = "123466";
        String name = "pengge";
        String sex = "FEMALE";
        int permission = 1;
        String tel = "911";

        User user = new User(id, username, password, name, sex, tel);

        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */

        when(rs.getInt("id")).thenReturn(id);
        when(rs.getString("username")).thenReturn(username);
        when(rs.getString("password")).thenReturn(password);
        when(rs.getString("name")).thenReturn(name);
        when(rs.getString("sex")).thenReturn(sex);
        when(rs.getString("tel")).thenReturn(tel);
        when(rs.getInt("permission")).thenReturn(permission);


        assertEquals(userList, this.userDao.queryAllUsers());

    }

    @Test
    void test_throws_sql_exception_when_query_user_by_id() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.userDao.queryUserById(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_query_User_By_Id_WhenPermissionIs_0() throws SQLException {
        int id = 305;
        String username = "PG";
        String password = "123466";
        String name = "pengge";
        String sex = "FEMALE";
        int permission = 0;
        String tel = "911";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */

        when(rs.getInt("id")).thenReturn(id);
        when(rs.getString("username")).thenReturn(username);
        when(rs.getString("password")).thenReturn(password);
        when(rs.getString("name")).thenReturn(name);
        when(rs.getString("sex")).thenReturn(sex);
        when(rs.getString("tel")).thenReturn(tel);
        when(rs.getInt("permission")).thenReturn(permission);
        User admin = this.userDao.queryUserById(5);
        assertAll(
                () -> assertEquals(username, admin.getUsername()),
                () -> assertEquals(password, admin.getPassword()),
                () -> assertEquals(name, admin.getName()),
                () -> assertEquals(permission, admin.getPermission()),
                () -> assertEquals(sex, admin.getSex().toString())
        );
        verify(preparedStatement).setInt(1, 5);
    }

    @Test
    void test_query_User_By_Id_WhenPermissionIs_1() throws SQLException {
        int id = 305;
        String username = "PG";
        String password = "123466";
        String name = "pengge";
        String sex = "FEMALE";
        int permission = 1;
        String tel = "911";

        User user = new User(5, username, password, name, sex, tel);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */

        when(rs.getString("username")).thenReturn(username);
        when(rs.getString("password")).thenReturn(password);
        when(rs.getString("name")).thenReturn(name);
        when(rs.getString("sex")).thenReturn(sex);
        when(rs.getString("tel")).thenReturn(tel);
        when(rs.getInt("permission")).thenReturn(permission);


        assertEquals(user, this.userDao.queryUserById(5));
        verify(preparedStatement).setInt(1, 5);
    }


    @Test
    void test_throws_sql_exception_when_query_user_by_name() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.userDao.queryUserByName("PG");
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_query_user_by_name_WhenPermissionIs_1() throws SQLException {
        int id = 305;
        String username = "PG";
        String password = "123466";
        String name = "pengge";
        String sex = "FEMALE";
        int permission = 1;
        String tel = "911";

        User user = new User(id, username, password, name, sex, tel);
        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */

        when(rs.getInt("id")).thenReturn(id);
        when(rs.getString("username")).thenReturn(username);
        when(rs.getString("password")).thenReturn(password);
        when(rs.getString("name")).thenReturn(name);
        when(rs.getString("sex")).thenReturn(sex);
        when(rs.getString("tel")).thenReturn(tel);
        when(rs.getInt("permission")).thenReturn(permission);


        assertEquals(userList, this.userDao.queryUserByName("PG"));
        verify(preparedStatement).setString(1, "PG");
    }

    @Test
    void test_throws_sql_exception_when_query_user_by_sex() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.userDao.queryUserBySex(Gender.FEMALE);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_query_user_by_sex_WhenPermissionIs_1() throws SQLException {
        int id = 305;
        String username = "PG";
        String password = "123456";
        String name = "pengge";
        String sex = "FEMALE";
        int permission = 1;
        String tel = "911";

        User user = new User(id, username, password, name, sex, tel);
        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */

        when(rs.getInt("id")).thenReturn(id);
        when(rs.getString("username")).thenReturn(username);
        when(rs.getString("password")).thenReturn(password);
        when(rs.getString("name")).thenReturn(name);
        when(rs.getString("sex")).thenReturn(sex);
        when(rs.getString("tel")).thenReturn(tel);
        when(rs.getInt("permission")).thenReturn(permission);

        assertEquals(userList, this.userDao.queryUserBySex(Gender.FEMALE));
        verify(preparedStatement).setString(1, "FEMALE");
    }


    @Test
    void test_throws_sql_exception_when_add_User() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.userDao.addUser(new User());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_add_User_When_Result_Is_True() throws SQLException {
        int id = 305;
        String username = "PG";
        String password = "123466";
        String name = "pengge";
        String sex = "FEMALE";
        int permission = 1;
        String tel = "911";

        User user = new User(id, username, password, name, sex, tel);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.userDao.addUser(user);

        assertEquals(result, true);
        verify(preparedStatement).setString(1, username);
        verify(preparedStatement).setString(2, password);
        verify(preparedStatement).setString(3, name);
        verify(preparedStatement).setString(4, sex);
        verify(preparedStatement).setInt(5, permission);
        verify(preparedStatement).setString(6, tel);

        verify(preparedStatement).executeUpdate();


    }


    @Test
    void test_delete_User_When_Result_Is_True() throws SQLException {

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.userDao.deleteUser(5);

        assertEquals(result, true);
        verify(preparedStatement).setInt(1, 5);
        verify(preparedStatement).executeUpdate();

    }


    @Test
    void test_throws_sql_exception_when_deleteUser() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.userDao.deleteUser(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_throws_sql_exception_when_updateUser() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        User user = new User();
        user.setId(5);
        this.userDao.updateUser(user);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_update_User_When_Result_Is_True() throws SQLException {
        int id = 305;
        String username = "PG";
        String password = "123466";
        String name = "pengge";
        String sex = "FEMALE";
        int permission = 1;
        String tel = "911";

        User user = new User(id, username, password, name, sex, tel);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.userDao.updateUser(user);

        assertEquals(result, true);
        verify(preparedStatement).setString(1, username);
        verify(preparedStatement).setString(2, password);
        verify(preparedStatement).setString(3, name);
        verify(preparedStatement).setString(4, sex);
        verify(preparedStatement).setInt(5, permission);
        verify(preparedStatement).setString(6, tel);
        verify(preparedStatement).setInt(7, id);

        verify(preparedStatement).executeUpdate();

    }
}