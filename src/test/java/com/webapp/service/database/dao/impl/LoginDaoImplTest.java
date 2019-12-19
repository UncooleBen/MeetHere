package com.webapp.service.database.dao.impl;

import com.webapp.model.user.Admin;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.LoginDao;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class LoginDaoImplTest {

    private LoginDao loginDao;
    private Connection connection = mock(Connection.class);
    private PreparedStatement preparedStatement = mock(PreparedStatement.class);
    private SQLException test_sql_exception;
    private ResultSet rs = mock(ResultSet.class);
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;



    class TestableLoginDaoImpl extends LoginDaoImpl
    {
        @Override
        public Connection getConnection() {
            return connection;
        }
    }

    @BeforeEach
    void init() {
        this.loginDao = new LoginDaoImplTest.TestableLoginDaoImpl();
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
    void test_throws_sql_exception_when_login() throws SQLException
    {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.loginDao.login("PG","123456");
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_throws_sql_exception_when_signup() throws SQLException
    {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.loginDao.signup(new User());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_login_when_PermissionIs_0() throws SQLException
    {
        int id=305;
        String username="PG";
        String password="123466";
        String name="pengge";
        String sex="FEMALE";
        int permission=0;
        String tel="911";

        Admin user=new Admin(id, username, password, name, sex, tel);
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

        assertEquals(this.loginDao.login("PG","123456"),user);
        verify(preparedStatement).setString(1,"PG");
        verify(preparedStatement).setString(2,"123456");

    }

    @Test
    void test_login_when_PermissionIs_1() throws SQLException
    {
        int id=305;
        String username="PG";
        String password="123466";
        String name="pengge";
        String sex="FEMALE";
        int permission=1;
        String tel="911";

        User user=new User(id, username, password, name, sex, tel);
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

        assertEquals(this.loginDao.login("PG","123456"),user);
        verify(preparedStatement).setString(1,"PG");
        verify(preparedStatement).setString(2,"123456");

    }

    @Test
    void test_signup() throws SQLException
    {
        int id=305;
        String username="PG";
        String password="123466";
        String name="pengge";
        String sex="FEMALE";
        int permission=1;
        String tel="911";

        User user=new User(id, username, password, name, sex, tel);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false, true);

        User result=this.loginDao.signup(user);
        assertEquals(result,user);
        verify(preparedStatement,times(2)).setString(1,username);
        verify(preparedStatement).setString(2,password);
        verify(preparedStatement).setString(3,name);
        verify(preparedStatement).setString(4,sex);
        verify(preparedStatement).setInt(5,1);
        verify(preparedStatement).setString(6,tel);
        verify(preparedStatement).execute();
    }

}