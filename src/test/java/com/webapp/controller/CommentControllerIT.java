package com.webapp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.webapp.config.MvcConfig;
import com.webapp.model.Comment;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.CommentDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Shangzhen Li
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfig.class)
public class CommentControllerIT {

  static final int totalCount = 6;
  final int[] userIdList = {1, 2, 3, 4, 5, 6};
  final long[] dateList = {1000, 2000, 3000, 4000, 5000, 6000};
  final String[] contentList = {
      "content from user 1",
      "content from user 2",
      "content from user 3",
      "content from user 4",
      "content from user 5",
      "content from user 6"
  };
  final int verifiedCount = 3;
  final String urlPrefix = "/WEB-INF/jsp/";
  @Autowired
  CommentDao commentDao;
  @Autowired
  WebApplicationContext wac;
  MockMvc mockMvc;
  int[] idList;

  static Stream<Arguments> userIllegalActionProvider() {
    if (totalCount > 0) {
      return Stream.of(
          arguments("delete", 0),
          arguments("delete", totalCount / 2),
          arguments("delete", totalCount - 1),
          arguments("verify", 0),
          arguments("verify", totalCount / 2),
          arguments("verify", totalCount - 1),
          arguments("other", 0),
          arguments("other", totalCount / 2),
          arguments("other", totalCount - 1));
    }
    return null;
  }

  static Stream<Arguments> adminIllegalActionProvider() {
    if (totalCount > 0) {
      return Stream.of(arguments("add"), arguments("save"), arguments("other"));
    }
    return null;
  }

  static Stream<Integer> idIndexProvider() {
    if (totalCount > 0) {
      return Stream.of(0, totalCount / 2, totalCount - 1);
    }
    return null;
  }

  @BeforeEach
  void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    // Add 3 unverified comments and 3 verified comments
    for (int i = 0; i < totalCount; i++) {
      this.commentDao.addComment(
          new Comment(this.userIdList[i], this.dateList[i], this.contentList[i]));
    }
    this.idList = new int[totalCount];
    List<Comment> addedCommentList = this.commentDao.listComment(totalCount, false);
    assertEquals(totalCount, addedCommentList.size());
    // Save added Comment id
    for (int i = 0; i < totalCount; i++) {
      this.idList[i] = addedCommentList.get(i).getId();
    }
    for (int i = 0; i < this.verifiedCount; i++) {
      addedCommentList.get(i).setVerified(true);
      assertTrue(this.commentDao.updateComment(addedCommentList.get(i)));
    }
  }

  @AfterEach
  void restore() {
    List<Comment> verifiedCommentList = this.commentDao.listComment(totalCount, true);
    List<Comment> unverifiedCommentList = this.commentDao.listComment(totalCount, false);
    for (Comment comment : verifiedCommentList) {
      this.commentDao.deleteComment(comment.getId());
    }
    for (Comment comment : unverifiedCommentList) {
      this.commentDao.deleteComment(comment.getId());
    }
  }

  @Test
  void givenUserWhenActionIsListThenListVerifiedComments() throws Exception {
    Map<String, Object> sessionAttr = new HashMap<>();
    sessionAttr.put("currentUserType", "user");
    MvcResult result =
        this.mockMvc
            .perform(get("/comment?action=list").sessionAttrs(sessionAttr))
            .andExpect(status().isOk())
            .andExpect(view().name("mainUser"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainUser.jsp"))
            .andExpect(model().attribute("mainPage", "user/comment.jsp"))
            .andReturn();
    List<Comment> verifiedCommentList =
        (List<Comment>) result.getModelAndView().getModelMap().get("commentList");
    List<Comment> unverifiedCommentList =
        (List<Comment>) result.getModelAndView().getModelMap().get("unverifiedCommentList");
    /*unverifiedCommentList should be null*/
    assertNull(unverifiedCommentList);
    for (int i = 0; i < this.verifiedCount; i++) {
      Comment temp = verifiedCommentList.get(i);
      int finalI = i; /*For lambda expression only*/
      assertAll(
          () -> assertEquals(this.userIdList[finalI], temp.getUserId()),
          () -> assertEquals(this.dateList[finalI], temp.getDate()),
          () -> assertEquals(this.contentList[finalI], temp.getContent()));
    }
  }

  @Test
  void givenAdminWhenActionIsListThenListAllComments() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result =
        this.mockMvc
            .perform(get("/comment?action=list").sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/comment.jsp"))
            .andReturn();
    List<Comment> verifiedCommentList =
        (List<Comment>) result.getModelAndView().getModelMap().get("commentList");
    List<Comment> unverifiedCommentList =
        (List<Comment>) result.getModelAndView().getModelMap().get("unverifiedCommentList");
    for (int i = 0; i < totalCount - this.verifiedCount; i++) {
      Comment temp = unverifiedCommentList.get(i);
      int finalI = i + this.verifiedCount; /*For lambda expression only*/
      assertAll(
          () -> assertEquals(this.userIdList[finalI], temp.getUserId()),
          () -> assertEquals(this.dateList[finalI], temp.getDate()),
          () -> assertEquals(this.contentList[finalI], temp.getContent()));
    }
    for (int i = 0; i < this.verifiedCount; i++) {
      Comment temp = verifiedCommentList.get(i);
      int finalI = i; /*For lambda expression only*/
      assertAll(
          () -> assertEquals(this.userIdList[finalI], temp.getUserId()),
          () -> assertEquals(this.dateList[finalI], temp.getDate()),
          () -> assertEquals(this.contentList[finalI], temp.getContent()));
    }
  }

  @ParameterizedTest
  @MethodSource("userIllegalActionProvider")
  void givenUserWhenIllegalActionThenReturnToBlank(String action, Integer idIndex)
      throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "user");
    this.mockMvc
        .perform(
            get("/comment?action=" + action + "&id=" + this.idList[idIndex])
                .sessionAttrs(sessionAttrs))
        .andExpect(status().isOk())
        .andExpect(view().name("mainUser"))
        .andExpect(forwardedUrl(this.urlPrefix + "mainUser.jsp"))
        .andExpect(model().attribute("mainPage", "user/blank.jsp"))
        .andReturn();
  }

  @ParameterizedTest
  @MethodSource("adminIllegalActionProvider")
  @Disabled
  void givenAdminWhenIllegalActionThenReturnToBlank(String action) throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    this.mockMvc
        .perform(get("/comment?action=" + action).sessionAttrs(sessionAttrs))
        .andExpect(status().isOk())
        .andExpect(view().name("mainAdmin"))
        .andExpect(forwardedUrl(urlPrefix + "mainAdmin.jsp"))
        .andExpect(model().attribute("mainPage", "admin/blank.jsp"))
        .andReturn();
  }

  @ParameterizedTest
  @MethodSource("idIndexProvider")
  void givenAdminWhenActionIsDeleteThenDeleteComment(Integer idIndex) throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result =
        this.mockMvc
            .perform(
                get("/comment?action=delete&id=" + this.idList[idIndex]).sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/comment.jsp"))
            .andReturn();
    List<Comment> verifiedCommentList =
        (List<Comment>) result.getModelAndView().getModelMap().get("commentList");
    List<Comment> unverifiedCommentList =
        (List<Comment>) result.getModelAndView().getModelMap().get("unverifiedCommentList");
    /*No comment's id can equals to the deleted one*/
    for (Comment comment : verifiedCommentList) {
      assertNotEquals(this.idList[idIndex], comment.getId());
    }
    for (Comment comment : unverifiedCommentList) {
      assertNotEquals(this.idList[idIndex], comment.getId());
    }
  }

  @Test
  void givenUserWhenActionIsAddThenRedirectToCommentSave() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "user");
    this.mockMvc
        .perform(get("/comment?action=add").sessionAttrs(sessionAttrs))
        .andExpect(status().isOk())
        .andExpect(view().name("mainUser"))
        .andExpect(forwardedUrl(urlPrefix + "mainUser.jsp"))
        .andExpect(model().attribute("mainPage", "user/commentSave.jsp"))
        .andReturn();
  }

  @Test
  void givenUserWhenActionIsSaveThenSave() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    final String TEST_CONTENT = "comment content to test add";
    final User TEST_USER = new User(0);
    sessionAttrs.put("currentUserType", "user");
    sessionAttrs.put("currentUser", TEST_USER);
    this.mockMvc
        .perform(
            post("/comment?action=save").sessionAttrs(sessionAttrs).param("content", TEST_CONTENT))
        .andExpect(status().isOk())
        .andExpect(view().name("mainUser"))
        .andExpect(forwardedUrl(urlPrefix + "mainUser.jsp"))
        .andExpect(model().attribute("mainPage", "user/comment.jsp"))
        .andReturn();
    int unverifiedCount = totalCount - this.verifiedCount + 1;
    List<Comment> commentList = this.commentDao.listComment(unverifiedCount, false);
    assertAll(
        () -> assertEquals(unverifiedCount, commentList.size()),
        () -> assertEquals(TEST_USER.getId(), commentList.get(unverifiedCount - 1).getUserId()),
        () -> assertEquals(TEST_CONTENT, commentList.get(unverifiedCount - 1).getContent()));
  }

  @ParameterizedTest
  @MethodSource("idIndexProvider")
  void givenAdminWhenActionIsVerifyThenVerify(Integer idIndex) throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result =
        this.mockMvc
            .perform(
                get("/comment?action=verify&id=" + this.idList[idIndex]).sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/comment.jsp"))
            .andReturn();
    List<Comment> verifiedCommentList =
        (List<Comment>) result.getModelAndView().getModelMap().get("commentList");
    List<Comment> unverifiedCommentList =
        (List<Comment>) result.getModelAndView().getModelMap().get("unverifiedCommentList");
    for (Comment comment : verifiedCommentList) {
      if (comment.getId() == this.idList[idIndex]) {
        assertTrue(comment.isVerified());
        break;
      }
    }
    for (Comment comment : unverifiedCommentList) {
      assertNotEquals(this.idList[idIndex], comment.getId());
    }
  }
}
