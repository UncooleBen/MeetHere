package com.webapp.service.database.dao.impl;

import com.webapp.model.News;
import com.webapp.service.database.dao.NewsDao;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NewsDaoImplTest {

    private NewsDao newsDao;
    private Connection connection = mock(Connection.class);
    private PreparedStatement preparedStatement = mock(PreparedStatement.class);
    private SQLException test_sql_exception;
    private ResultSet rs = mock(ResultSet.class);
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;


    class TestableNewsDaoImpl extends NewsDaoImpl {
        @Override
        public Connection getConnection() {
            return connection;
        }
    }

    @BeforeEach
    void init() {
        this.newsDao = new TestableNewsDaoImpl();
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
    void test_throws_sql_exception_when_list_news() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.newsDao.listNews(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_when_list_news() throws SQLException {
        int id = 305;
        String title = "PGNB";
        long created = 4456;
        long last_modified = 4456;
        String author = "PG";
        String detail = "PG666";

        News news = new News(id, title, created, last_modified, author, detail);

        List<News> newsList = new ArrayList<>();
        newsList.add(news);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
        when(rs.getInt("id")).thenReturn(id);
        when(rs.getString("title")).thenReturn(title);
        when(rs.getLong("created")).thenReturn(created);
        when(rs.getLong("last_modified")).thenReturn(last_modified);
        when(rs.getString("author")).thenReturn(author);
        when(rs.getString("detail")).thenReturn(detail);


        assertEquals(newsList, this.newsDao.listNews(5));
        verify(preparedStatement).setInt(1, 5);

    }

    @Test
    void test_throws_sql_exception_when_query_news_by_id() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.newsDao.queryNewsById(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_query_News_By_Id() throws SQLException {
        int id = 305;
        String title = "PGNB";
        long created = 4456;
        long last_modified = 4456;
        String author = "PG";
        String detail = "PG666";

        News news = new News(id, title, created, last_modified, author, detail);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
        when(rs.getInt("id")).thenReturn(id);
        when(rs.getString("title")).thenReturn(title);
        when(rs.getLong("created")).thenReturn(created);
        when(rs.getLong("last_modified")).thenReturn(last_modified);
        when(rs.getString("author")).thenReturn(author);
        when(rs.getString("detail")).thenReturn(detail);


        assertEquals(news, this.newsDao.queryNewsById(5));
        verify(preparedStatement).setInt(1, 5);
    }

    @Test
    void test_throws_sql_exception_when_insert_News() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.newsDao.insertNews(new News());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_insert_News_When_Result_Is_True() throws SQLException {
        int id = 305;
        String title = "PGNB";
        long created = 4456;
        long last_modified = 4456;
        String author = "PG";
        String detail = "PG666";

        News news = new News(id, title, created, last_modified, author, detail);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.newsDao.insertNews(news);

        assertEquals(result, true);
        verify(preparedStatement).setString(1, title);
        verify(preparedStatement).setLong(2, created);
        verify(preparedStatement).setLong(3, last_modified);
        verify(preparedStatement).setString(4, author);
        verify(preparedStatement).setString(5, detail);


    }


    @Test
    void test_delete_News_When_Result_Is_True() throws SQLException {

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.newsDao.deleteNewsById(5);

        assertEquals(result, true);
        verify(preparedStatement).setInt(1, 5);
    }


    @Test
    void test_throws_sql_exception_when_deleteNews() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.newsDao.deleteNewsById(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_throws_sql_exception_when_updateNews() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.newsDao.updateNews(new News());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_update_News_When_Result_Is_True() throws SQLException {
        int id = 305;
        String title = "PGNB";
        long created = 4456;
        long last_modified = 4456;
        String author = "PG";
        String detail = "PG666";

        News news = new News(id, title, created, last_modified, author, detail);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.newsDao.updateNews(news);

        assertEquals(result, true);
        verify(preparedStatement).setString(1, title);
        verify(preparedStatement).setLong(2, created);
        verify(preparedStatement).setLong(3, last_modified);
        verify(preparedStatement).setString(4, author);
        verify(preparedStatement).setString(5, detail);
        verify(preparedStatement).setInt(6, id);

    }


}