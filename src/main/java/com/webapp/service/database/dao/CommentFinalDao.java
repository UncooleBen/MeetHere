package com.webapp.service.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.webapp.model.Comment;
import com.webapp.util.StringUtil;

public class CommentFinalDao {
    public List<Comment> commentList(Connection con, Comment s_comment)throws Exception {
        List<Comment> commentList = new ArrayList<Comment>();
        StringBuffer sb = new StringBuffer("select * from t_comment_final t1");
        if(StringUtil.isNotEmpty(s_comment.getUserNumber())) {
            sb.append(" and t1.userNumber like '%"+s_comment.getUserNumber()+"%'");
        }
        if(StringUtil.isNotEmpty(s_comment.getDate())) {
            sb.append(" and t1.date="+s_comment.getDate());
        }
        if(StringUtil.isNotEmpty(s_comment.getStartDate())){
            sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_comment.getStartDate()+"')");
        }
        if(StringUtil.isNotEmpty(s_comment.getEndDate())){
            sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_comment.getEndDate()+"')");
        }
        PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            Comment comment=new Comment();
            comment.setCommentId(rs.getInt("commentId"));
            comment.setUserNumber(rs.getString("userNumber"));
            comment.setDate(rs.getString("date"));
            comment.setDetail(rs.getString("detail"));
            commentList.add(comment);
        }
        return commentList;
    }

    public List<Comment> commentListWithBuild(Connection con, Comment s_comment, int buildId)throws Exception {
        List<Comment> commentList = new ArrayList<Comment>();
        StringBuffer sb = new StringBuffer("select * from t_comment_final t1");
        if(StringUtil.isNotEmpty(s_comment.getUserNumber())) {
            sb.append(" and t1.userNumber like '%"+s_comment.getUserNumber()+"%'");
        }
        if(StringUtil.isNotEmpty(s_comment.getStartDate())){
            sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_comment.getStartDate()+"')");
        }
        if(StringUtil.isNotEmpty(s_comment.getEndDate())){
            sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_comment.getEndDate()+"')");
        }
        PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            Comment comment=new Comment();
            comment.setCommentId(rs.getInt("commentId"));
            comment.setUserNumber(rs.getString("userNumber"));
            comment.setDate(rs.getString("date"));
            comment.setDetail(rs.getString("detail"));
            commentList.add(comment);
        }
        return commentList;
    }

    public List<Comment> commentListWithNumber(Connection con, Comment s_comment, String userNumber)throws Exception {
        List<Comment> commentList = new ArrayList<Comment>();
        StringBuffer sb = new StringBuffer("select * from t_comment_final t1");
        if(StringUtil.isNotEmpty(userNumber)) {
            sb.append(" and t1.userNumber ="+userNumber);
        }
        if(StringUtil.isNotEmpty(s_comment.getStartDate())){
            sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_comment.getStartDate()+"')");
        }
        if(StringUtil.isNotEmpty(s_comment.getEndDate())){
            sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_comment.getEndDate()+"')");
        }
        PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            Comment comment=new Comment();
            comment.setCommentId(rs.getInt("commentId"));
            comment.setUserNumber(rs.getString("userNumber"));
            comment.setDate(rs.getString("date"));
            comment.setDetail(rs.getString("detail"));
            commentList.add(comment);
        }
        return commentList;
    }

    public Comment commentShow(Connection con, String commentId)throws Exception {
        String sql = "select * from t_comment_final t1 where t1.commentId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, commentId);
        ResultSet rs=pstmt.executeQuery();
        Comment comment = new Comment();
        if(rs.next()) {
            comment.setCommentId(rs.getInt("commentId"));
            comment.setUserNumber(rs.getString("userNumber"));
            comment.setDate(rs.getString("date"));
            comment.setDetail(rs.getString("detail"));
        }
        return comment;
    }

    public int commentAdd(Connection con, Comment comment)throws Exception {
        String sql = "insert into t_comment_final values(null,?,?,?)";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, comment.getUserNumber());
        pstmt.setString(2, comment.getDate());
        pstmt.setString(3, comment.getDetail());
        return pstmt.executeUpdate();
    }

    public int commentDelete(Connection con, String commentId)throws Exception {
        String sql = "delete from t_comment_final where commentId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, commentId);
        return pstmt.executeUpdate();
    }

    public int commentUpdate(Connection con, Comment comment)throws Exception {
        String sql = "update t_comment_final set userNumber=?,detail=? where commentId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, comment.getUserNumber());
        pstmt.setString(2, comment.getDetail());
        pstmt.setInt(3, comment.getCommentId());
        return pstmt.executeUpdate();
    }


}
