package com.webapp.service.database.dao.impl;

import com.webapp.model.Comment;
import com.webapp.service.database.dao.CommentDao;
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

class CommentDaoImplTest {
    private CommentDao commentDao;
    private Connection connection = mock(Connection.class);
    private PreparedStatement preparedStatement = mock(PreparedStatement.class);
    private SQLException test_sql_exception;
    private ResultSet rs = mock(ResultSet.class);
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;


    class TestableCommentDaoImpl extends CommentDaoImpl {
        @Override
        public Connection getConnection() {
            return connection;
        }
    }

    @BeforeEach
    void init() {
        this.commentDao = new TestableCommentDaoImpl();
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
    void test_throws_sql_exception_when_list_comment() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.commentDao.listComment(5, true);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_when_list_comment() throws SQLException {
        int id = 305;
        int userid = 13;
        long date = 15000;
        String content = "PGNB";

        Comment comment = new Comment();
        comment.setId(id);
        comment.setUserId(userid);
        comment.setDate(date);
        comment.setContent(content);

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
        when(rs.getInt("id")).thenReturn(id);
        when(rs.getInt("userId")).thenReturn(userid);
        when(rs.getLong("date")).thenReturn(date);
        when(rs.getString("content")).thenReturn(content);


        assertEquals(commentList, this.commentDao.listComment(5, true));
        verify(preparedStatement).setBoolean(1, true);
        verify(preparedStatement).setInt(2, 5);

    }

    @Test
    void test_throws_sql_exception_when_query_comment_by_id() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.commentDao.queryCommentById(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_query_Comment_By_Id() throws SQLException {
        int id = 305;
        int userid = 13;
        long date = 15000;
        String content = "PGNB";

        Comment comment = new Comment();
        comment.setId(id);
        comment.setUserId(userid);
        comment.setDate(date);
        comment.setContent(content);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
        when(rs.getInt("id")).thenReturn(id);
        when(rs.getInt("userId")).thenReturn(userid);
        when(rs.getLong("date")).thenReturn(date);
        when(rs.getString("content")).thenReturn(content);


        assertEquals(comment, this.commentDao.queryCommentById(5));
        verify(preparedStatement).setInt(1, 5);
    }

    @Test
    void test_throws_sql_exception_when_add_Comment() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.commentDao.addComment(new Comment());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_add_Comment_When_Result_Is_True() throws SQLException {
        int id = 305;
        int userid = 13;
        long date = 15000;
        String content = "PGNB";

        Comment comment = new Comment();
        comment.setId(id);
        comment.setUserId(userid);
        comment.setDate(date);
        comment.setContent(content);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.commentDao.addComment(comment);

        assertEquals(result, true);
        verify(preparedStatement).setInt(1, userid);
        verify(preparedStatement).setLong(2, date);
        verify(preparedStatement).setString(3, content);


    }

    @Test
    void test_add_Comment_When_Result_Is_False() throws SQLException {
        int id = 305;
        int userid = 13;
        long date = 15000;
        String content = "PGNB";

        Comment comment = new Comment();
        comment.setId(id);
        comment.setUserId(userid);
        comment.setDate(date);
        comment.setContent(content);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = this.commentDao.addComment(comment);

        assertEquals(result, false);
        verify(preparedStatement).setInt(1, userid);
        verify(preparedStatement).setLong(2, date);
        verify(preparedStatement).setString(3, content);


    }

    @Test
    void test_delete_Comment_When_Result_Is_True() throws SQLException {

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.commentDao.deleteComment(5);

        assertEquals(result, true);
        verify(preparedStatement).setInt(1, 5);
    }

    @Test
    void test_delete_Comment_When_Result_Is_False() throws SQLException {

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = this.commentDao.deleteComment(5);

        assertEquals(result, false);
        verify(preparedStatement).setInt(1, 5);
    }

    @Test
    void test_throws_sql_exception_when_deleteComment() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.commentDao.deleteComment(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_throws_sql_exception_when_updateComment() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.commentDao.updateComment(new Comment());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_update_Comment_When_Result_Is_True() throws SQLException {
        int id = 305;
        int userid = 13;
        long date = 15000;
        String content = "PGNB";

        Comment comment = new Comment();
        comment.setId(id);
        comment.setUserId(userid);
        comment.setDate(date);
        comment.setContent(content);
        comment.setVerified(true);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.commentDao.updateComment(comment);

        assertEquals(result, true);
        verify(preparedStatement).setInt(1, userid);
        verify(preparedStatement).setLong(2, date);
        verify(preparedStatement).setString(3, content);
        verify(preparedStatement).setBoolean(4, true);
        verify(preparedStatement).setInt(5, id);

    }

    @Test
    void test_update_Comment_When_Result_Is_False() throws SQLException {
        int id = 305;
        int userid = 13;
        long date = 15000;
        String content = "PGNB";

        Comment comment = new Comment();
        comment.setId(id);
        comment.setUserId(userid);
        comment.setDate(date);
        comment.setContent(content);
        comment.setVerified(true);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = this.commentDao.updateComment(comment);

        assertEquals(result, false);
        verify(preparedStatement).setInt(1, userid);
        verify(preparedStatement).setLong(2, date);
        verify(preparedStatement).setString(3, content);
        verify(preparedStatement).setBoolean(4, true);
        verify(preparedStatement).setInt(5, id);

    }


}