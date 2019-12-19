package com.webapp.service.database.dao.impl;

import com.webapp.model.Comment;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.CommentDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements CommentDao interface to interact with table 't_comment' in the database.
 *
 * @author Juntao Peng (original creator)
 * @author Shangzhen Li (refactor)
 */
public class CommentDaoImpl extends DatabaseService implements CommentDao {

  public CommentDaoImpl() {}

  @Override
  public List<Comment> listComment(int size, boolean verified) {
    List<Comment> commentList = new ArrayList<Comment>();
      String sql = "SELECT * FROM t_comment WHERE verified = ? LIMIT ?";
      Connection connection = getConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setBoolean(1, verified);
      preparedStatement.setInt(2, size);
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        Comment comment = new Comment();
        comment.setId(rs.getInt("id"));
        comment.setUserId(rs.getInt("userId"));
        comment.setDate(rs.getLong("date"));
        comment.setContent(rs.getString("content"));
        commentList.add(comment);
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace(System.err);
    } finally {
      closeConnection(connection);
    }
    return commentList;
  }

//  @Override
//  public List<Comment> queryCommentByUserId(int userId, boolean verified) {
//    List<Comment> commentList = new ArrayList<>();
//    String sql = "SELECT * FROM t_comment WHERE userId = ? AND verified=? ";
//    Connection connection = getConnection();
//    try {
//      PreparedStatement preparedStatement = connection.prepareStatement(sql);
//      preparedStatement.setInt(1, userId);
//      preparedStatement.setBoolean(2, verified);
//      ResultSet rs = preparedStatement.executeQuery();
//      while (rs.next()) {
//        Comment comment = new Comment();
//        comment.setId(rs.getInt("id"));
//        comment.setUserId(rs.getInt("userId"));
//        comment.setDate(rs.getLong("date"));
//          comment.setContent(rs.getString("content"));
//          comment.setVerified(rs.getBoolean("verified"));
//          commentList.add(comment);
//      }
//    } catch (SQLException sqlException) {
//      sqlException.printStackTrace(System.err);
//    } finally {
//      closeConnection(connection);
//    }
//    return commentList;
//  }

  @Override
  public Comment queryCommentById(int commentId) {
    String sql = "SELECT * FROM t_comment t1 WHERE t1.id=?";
    Connection connection = getConnection();
    Comment comment = null;
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, commentId);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        comment = new Comment();
        comment.setId(rs.getInt("id"));
        comment.setUserId(rs.getInt("userId"));
        comment.setDate(rs.getLong("date"));
        comment.setContent(rs.getString("content"));
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.err);
    } finally {
      closeConnection(connection);
    }
    return comment;
  }

  @Override
  public boolean addComment(Comment comment) {
    String sql = "INSERT INTO t_comment(userId,date,content) VALUES (?,?,?)";
    Connection connection = getConnection();
    int result = 0;
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, comment.getUserId());
      preparedStatement.setLong(2, comment.getDate());
      preparedStatement.setString(3, comment.getContent());
      result = preparedStatement.executeUpdate();
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.err);
    } finally {
      closeConnection(connection);
    }
    if (0 != result) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean deleteComment(int commentId) {
    String sql = "DELETE FROM t_comment WHERE id=?";
    Connection connection = getConnection();
    int result = 0;
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, commentId);
      result = preparedStatement.executeUpdate();
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.err);
    } finally {
      closeConnection(connection);
    }
    if (0 != result) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean updateComment(Comment comment) {
      String sql = "UPDATE t_comment SET userId = ?, date = ?, content = ?, verified = ? WHERE id=?";
      Connection connection = getConnection();
    int result = 0;
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, comment.getUserId());
        preparedStatement.setLong(2, comment.getDate());
        preparedStatement.setString(3, comment.getContent());
        preparedStatement.setBoolean(4, comment.isVerified());
        preparedStatement.setInt(5, comment.getId());
        result = preparedStatement.executeUpdate();
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.err);
    } finally {
      closeConnection(connection);
    }
    if (0 != result) {
      return true;
    } else {
      return false;
    }
  }
}
