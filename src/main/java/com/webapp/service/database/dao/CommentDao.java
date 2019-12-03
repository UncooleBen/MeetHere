package com.webapp.service.database.dao;

import com.webapp.model.Comment;
import java.util.List;

public interface CommentDao {
  List<Comment> listComment(int size, boolean verified);

  List<Comment> queryCommentByBuildingId(int buildId, boolean verified);

  List<Comment> queryCommentByUserId(int userId, boolean verified);

  Comment queryCommentById(int commentId, boolean verified);

  boolean addComment(Comment comment);

  boolean deleteComment(String commentId);

  boolean updateComment(Comment comment);
}
