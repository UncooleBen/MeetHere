package com.webapp.service.database.dao;

import com.webapp.model.Comment;
import java.sql.Connection;
import java.util.List;

public interface CommentDao {
  List<Comment> commentList(Connection con, Comment s_comment);
  List<Comment> commentListWithBuild(Connection con, Comment s_comment, int buildId);
  List<Comment> commentListWithNumber(Connection con, Comment s_comment, String userNumber);
  Comment commentShow(Connection con, String commentId);
  int commentAdd(Connection con, Comment comment);
  int commentDelete(Connection con, String commentId);
  int commentUpdate(Connection con, Comment comment);
}
