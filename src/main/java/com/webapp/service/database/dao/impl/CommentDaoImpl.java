package com.webapp.service.database.dao.impl;

import com.webapp.model.Comment;
import com.webapp.model.Record;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.CommentDao;
import com.webapp.util.StringUtil;
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
    private Connection connection;

    public CommentDaoImpl() {
        this.connection = getConnection();
    }

    @Override
    public List<Comment> listComment(int size) {
        List<Comment> commentList = new ArrayList<Comment>();
        String SQL="SELECT * FROM t_comment LIMIT ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, size);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("commentId"));
                comment.setUserId(rs.getInt("userNumber"));
                comment.setDate(rs.getLong("date"));
                comment.setContent(rs.getString("detail"));
                commentList.add(comment);
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace(System.err);
        }

        return commentList;
    }

    @Override
    public List<Comment> queryCommentByUserId(int userId) {
        assert connection != null;
        List<Comment> commentList = new ArrayList<>();
        String SELECT = "SELECT * FROM t_comment WHERE userId = ? AND verified ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                result = new Record(rs.getInt("id"), rs.getLong("time"), rs.getLong("start_date"),
                        rs.getLong("end_date"), rs.getInt("user_id"), rs.getInt("building_id"),
                        rs.getBoolean("verified"));
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }

    }

    @Override
    public Comment queryCommentById(int commentId, boolean verified) {
        String sql = "select * from t_comment t1 where t1.commentId=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, commentId);
        ResultSet rs = preparedStatement.executeQuery();
        Comment comment = new Comment();
        if (rs.next()) {
            comment.setId(rs.getInt("commentId"));
            comment.setUserId(rs.getString("userNumber"));
            comment.setDate(rs.getString("date"));
            comment.setContent(rs.getString("detail"));
        }
        return comment;
    }

    @Override
    public boolean addComment(Comment comment) {
        String sql = "insert into t_comment values(null,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, comment.getUserNumber());
        preparedStatement.setString(2, comment.getDate());
        preparedStatement.setString(3, comment.getDetail());
        return preparedStatement.executeUpdate();
    }

    @Override
    public boolean deleteComment(String commentId) {
        String sql = "delete from t_comment where commentId=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, commentId);
        return preparedStatement.executeUpdate();
    }

    @Override
    public boolean updateComment(Comment comment) {
        String sql = "update t_comment set userNumber=?,detail=? where commentId=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, comment.getUserNumber());
        preparedStatement.setString(2, comment.getDetail());
        preparedStatement.setInt(3, comment.getCommentId());
        return preparedStatement.executeUpdate();
    }
}
