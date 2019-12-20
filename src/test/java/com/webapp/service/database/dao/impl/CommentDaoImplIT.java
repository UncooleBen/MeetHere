package com.webapp.service.database.dao.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webapp.model.Comment;
import com.webapp.service.database.dao.CommentDao;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class CommentDaoImplIT {

  private final int TEST_USERID_0 = 0;
  private final long TEST_DATE_0 = 1000;
  private final String TEST_CONTENT_0 = "test content 0";
  private final int TEST_USERID_1 = 1;
  private final long TEST_DATE_1 = 2000;
  private final String TEST_CONTENT_1 = "test content 1";
  private final int TEST_USERID_2 = 2;
  private final long TEST_DATE_2 = 3000;
  private final String TEST_CONTENT_2 = "test content 2";
  private CommentDao commentDao;

  @BeforeEach
  void init() {
    commentDao = new CommentDaoImpl();
    String DB_URL =
        "jdbc:mysql://localhost:3306/meethere?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
    ReflectionTestUtils.setField(commentDao, "dbUrl", DB_URL);
    String DB_USERNAME = "root";
    ReflectionTestUtils.setField(commentDao, "dbUsername", DB_USERNAME);
    String DB_PASSWORD = "root";
    ReflectionTestUtils.setField(commentDao, "dbPassword", DB_PASSWORD);
    String DB_CLASSNAME = "com.mysql.jdbc.Driver";
    ReflectionTestUtils.setField(commentDao, "dbClassname", DB_CLASSNAME);
  }

  @Test
  void add3CommentsAndList5ShouldGet3UnverifiedComments() {
    // Add test comments to database
    Comment testComment0 = new Comment(TEST_USERID_0, TEST_DATE_0, TEST_CONTENT_0);
    Comment testComment1 = new Comment(TEST_USERID_1, TEST_DATE_1, TEST_CONTENT_1);
    Comment testComment2 = new Comment(TEST_USERID_2, TEST_DATE_2, TEST_CONTENT_2);
    assertAll(
        () -> assertTrue(commentDao.addComment(testComment0)),
        () -> assertTrue(commentDao.addComment(testComment1)),
        () -> assertTrue(commentDao.addComment(testComment2)));
    // Should be 3 unverified and 0 verified
    List<Comment> verifiedCommentList = commentDao.listComment(5, true);
    List<Comment> unverifiedCommentList = commentDao.listComment(5, false);
    assertAll(
        () -> assertEquals(0, verifiedCommentList.size()),
        () -> assertEquals(3, unverifiedCommentList.size()));
    Comment returnedComment0 = unverifiedCommentList.get(0);
    Comment returnedComment1 = unverifiedCommentList.get(1);
    Comment returnedComment2 = unverifiedCommentList.get(2);
    assertAll(
        () -> assertEquals(TEST_USERID_0, returnedComment0.getUserId()),
        () -> assertEquals(TEST_DATE_0, returnedComment0.getDate()),
        () -> assertEquals(TEST_CONTENT_0, returnedComment0.getContent()),
        () -> assertEquals(TEST_USERID_1, returnedComment1.getUserId()),
        () -> assertEquals(TEST_DATE_1, returnedComment1.getDate()),
        () -> assertEquals(TEST_CONTENT_1, returnedComment1.getContent()),
        () -> assertEquals(TEST_USERID_2, returnedComment2.getUserId()),
        () -> assertEquals(TEST_DATE_2, returnedComment2.getDate()),
        () -> assertEquals(TEST_CONTENT_2, returnedComment2.getContent()),
        // Delete test data from database
        () -> assertTrue(commentDao.deleteComment(returnedComment0.getId())),
        () -> assertTrue(commentDao.deleteComment(returnedComment1.getId())),
        () -> assertTrue(commentDao.deleteComment(returnedComment2.getId())));
  }

  @Test
  void add3CommentsAndList2ShouldGet2UnverifiedComments() {
    // Add test comments to database
    Comment testComment0 = new Comment(TEST_USERID_0, TEST_DATE_0, TEST_CONTENT_0);
    Comment testComment1 = new Comment(TEST_USERID_1, TEST_DATE_1, TEST_CONTENT_1);
    Comment testComment2 = new Comment(TEST_USERID_2, TEST_DATE_2, TEST_CONTENT_2);
    assertAll(
        () -> assertTrue(commentDao.addComment(testComment0)),
        () -> assertTrue(commentDao.addComment(testComment1)),
        () -> assertTrue(commentDao.addComment(testComment2)));
    // Should be 2 unverified and 0 verified
    List<Comment> verifiedCommentList = commentDao.listComment(2, true);
    List<Comment> unverifiedCommentList = commentDao.listComment(2, false);
    assertAll(
        () -> assertEquals(0, verifiedCommentList.size()),
        () -> assertEquals(2, unverifiedCommentList.size()));
    Comment returnedComment0 = unverifiedCommentList.get(0);
    Comment returnedComment1 = unverifiedCommentList.get(1);
    assertAll(
        () -> assertEquals(TEST_USERID_0, returnedComment0.getUserId()),
        () -> assertEquals(TEST_DATE_0, returnedComment0.getDate()),
        () -> assertEquals(TEST_CONTENT_0, returnedComment0.getContent()),
        () -> assertEquals(TEST_USERID_1, returnedComment1.getUserId()),
        () -> assertEquals(TEST_DATE_1, returnedComment1.getDate()),
        () -> assertEquals(TEST_CONTENT_1, returnedComment1.getContent()),
        // Delete test data from database
        () -> assertTrue(commentDao.deleteComment(returnedComment0.getId())),
        () -> assertTrue(commentDao.deleteComment(returnedComment1.getId())),
        () -> assertTrue(commentDao.deleteComment(returnedComment1.getId() + 1)));
  }

  @Test
  void add1CommentAndQueryById() {
    // Add test data to database
    Comment testComment = new Comment(TEST_USERID_0, TEST_DATE_0, TEST_CONTENT_0);
    assertAll(() -> assertTrue(commentDao.addComment(testComment)));
    List<Comment> unverifiedCommentList = commentDao.listComment(1, false);
    assertAll(() -> assertEquals(1, unverifiedCommentList.size()));
    // Execute query
    int id = unverifiedCommentList.get(0).getId();
    Comment returnedComment = commentDao.queryCommentById(id);
    assertAll(
        () -> assertEquals(TEST_USERID_0, returnedComment.getUserId()),
        () -> assertEquals(TEST_DATE_0, returnedComment.getDate()),
        () -> assertEquals(TEST_CONTENT_0, returnedComment.getContent()),
        // Delete test data from database
        () -> assertTrue(commentDao.deleteComment(id)));
  }

  @Test
  void add1CommentAndUpdate() {
    // Add test data to database
    Comment testComment = new Comment(TEST_USERID_0, TEST_DATE_0, TEST_CONTENT_0);
    assertAll(() -> assertTrue(commentDao.addComment(testComment)));
    List<Comment> unverifiedCommentList = commentDao.listComment(1, false);
    assertAll(() -> assertEquals(1, unverifiedCommentList.size()));
    // Execute update
    Comment newComment = unverifiedCommentList.get(0);
    int id = newComment.getId();
    // Only 'verified' can be updated
    newComment.setVerified(true);
    assertAll(() -> assertTrue(commentDao.updateComment(newComment)));
    Comment updatedComment = commentDao.queryCommentById(id);
    assertAll(
        () -> assertEquals(id, newComment.getId()),
        () -> assertEquals(TEST_USERID_0, newComment.getUserId()),
        () -> assertEquals(TEST_DATE_0, newComment.getDate()),
        () -> assertEquals(TEST_CONTENT_0, newComment.getContent()),
        // Delete test data from database
        () -> assertTrue(commentDao.deleteComment(id)));
  }

  @AfterEach
  void restore() {
  }
}
