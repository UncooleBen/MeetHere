package com.webapp.service.database.dao.impl;

import com.webapp.model.Comment;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.CommentDao;
import com.webapp.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements CommentDao interface to interact with table 't_comment' in the database.
 *
 * @author Juntao Peng (original creator)
 * @author Shangzhen Li (refactor)
 */
public class CommentDaoImpl extends DatabaseService implements CommentDao {
  private Connection connection;

  public CommentDaoImpl() {
    this.connection = getConnection();
  }

  @Override
  public List<Comment> listComment(int size, boolean verified) {
    List<Comment> commentList = new ArrayList<Comment>();
    String SQL="SELECT * FROM t_comment LIMIT ?";
    try{
      PreparedStatement pstmt = connection.prepareStatement(SQL);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        Comment comment = new Comment();
        comment.setCommentId(rs.getInt("commentId"));
        comment.setUserNumber(rs.getString("userNumber"));
        comment.setDate(rs.getString("date"));
        comment.setDetail(rs.getString("detail"));
        commentList.add(comment);
      }
    }catch (Exception e){
      System.out.println(e.getMessage());
    }

    return commentList;
  }

  @Override
  public List<Comment> queryCommentByBuildingId(int buildId, boolean verified) {
    List<Comment> commentList = new ArrayList<Comment>();
    StringBuffer sb = new StringBuffer("select * from t_comment t1");
    if (StringUtil.isNotEmpty(s_comment.getUserNumber())) {
      sb.append(" and t1.userNumber like '%" + s_comment.getUserNumber() + "%'");
    }
    if (StringUtil.isNotEmpty(s_comment.getStartDate())) {
      sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('" + s_comment.getStartDate() + "')");
    }
    if (StringUtil.isNotEmpty(s_comment.getEndDate())) {
      sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('" + s_comment.getEndDate() + "')");
    }
    PreparedStatement pstmt = connection.prepareStatement(sb.toString().replaceFirst("and", "where"));
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
      Comment comment = new Comment();
      comment.setCommentId(rs.getInt("commentId"));
      comment.setUserNumber(rs.getString("userNumber"));
      comment.setDate(rs.getString("date"));
      comment.setDetail(rs.getString("detail"));
      commentList.add(comment);
    }
    return commentList;
  }

  @Override
  public List<Comment> queryCommentByUserId(int userId, boolean verified) {
    List<Comment> commentList = new ArrayList<Comment>();
    StringBuffer sb = new StringBuffer("select * from t_comment t1");
    if (StringUtil.isNotEmpty(userNumber)) {
      sb.append(" and t1.userNumber =" + userNumber);
    }
    if (StringUtil.isNotEmpty(s_comment.getStartDate())) {
      sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('" + s_comment.getStartDate() + "')");
    }
    if (StringUtil.isNotEmpty(s_comment.getEndDate())) {
      sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('" + s_comment.getEndDate() + "')");
    }
    PreparedStatement pstmt = connection.prepareStatement(sb.toString().replaceFirst("and", "where"));
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
      Comment comment = new Comment();
      comment.setCommentId(rs.getInt("commentId"));
      comment.setUserNumber(rs.getString("userNumber"));
      comment.setDate(rs.getString("date"));
      comment.setDetail(rs.getString("detail"));
      commentList.add(comment);
    }
    return commentList;
  }

  @Override
  public Comment queryCommentById(int commentId, boolean verified) {
    String sql = "select * from t_comment t1 where t1.commentId=?";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setString(1, commentId);
    ResultSet rs = pstmt.executeQuery();
    Comment comment = new Comment();
    if (rs.next()) {
      comment.setCommentId(rs.getInt("commentId"));
      comment.setUserNumber(rs.getString("userNumber"));
      comment.setDate(rs.getString("date"));
      comment.setDetail(rs.getString("detail"));
    }
    return comment;
  }

  @Override
  public boolean addComment(Comment comment) {
    String sql = "insert into t_comment values(null,?,?,?)";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setString(1, comment.getUserNumber());
    pstmt.setString(2, comment.getDate());
    pstmt.setString(3, comment.getDetail());
    return pstmt.executeUpdate();
  }

  @Override
  public boolean deleteComment(String commentId) {
    String sql = "delete from t_comment where commentId=?";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setString(1, commentId);
    return pstmt.executeUpdate();
  }

  @Override
  public boolean updateComment(Comment comment) {
    String sql = "update t_comment set userNumber=?,detail=? where commentId=?";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setString(1, comment.getUserNumber());
    pstmt.setString(2, comment.getDetail());
    pstmt.setInt(3, comment.getCommentId());
    return pstmt.executeUpdate();
  }
}
