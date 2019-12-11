package com.webapp.service.database.dao;

import com.webapp.model.Comment;
import java.util.List;

/**
 * This interface declares methods used to interact with table 't_comment' in the database.
 *
 * @author Juntao Peng (original creator)
 * @author Shangzhen Li (refactor)
 */
public interface CommentDao {

  /**
   * Get a list of comments in 't_comment' given size and verified.
   *
   * @param size The size of list
   * @param verified A flag whether comments are verified or not
   * @return A list of comments
   */
  List<Comment> listComment(int size, boolean verified);

  /**
   * Query a list comments in 't_comment' given user id and verified.
   *
   * @param userId The related user id
   * @param verified A flag whether comments are verified or not
   * @return A list of comment.
  List<Comment> queryCommentByUserId(int userId, boolean verified);
   */

  /**
   * Query a comment in 't_comment' given id.
   *
   * @param commentId The id
   * @return A comment
   */
  Comment queryCommentById(int commentId);

  /**
   * Added a comment in 't_comment'
   *
   * @param comment The commend to add
   * @return True if succeeded, otherwise false
   */
  boolean addComment(Comment comment);

  /**
   * Delete a comment in 't_comment' given comment id
   *
   * @param commentId The commend id to delete
   * @return True if succeeded, otherwise false
   */
  boolean deleteComment(int commentId);

  /**
   * Update a comment in 't_comment'
   *
   * @param comment The commend to add
   * @return True if succeeded, otherwise false
   */
  boolean updateComment(Comment comment);
}
