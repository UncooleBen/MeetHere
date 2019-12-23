package com.webapp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.webapp.model.News;
import com.webapp.service.database.dao.NewsDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
public class NewsControllerIT {

  final String urlPrefix = "/WEB-INF/jsp/";
  MockMvc mockMvc;
  @Autowired
  NewsDao newsDao;
  @Autowired
  WebApplicationContext wac;
  News testNews;

  static Stream<Arguments> illegalUserTypeAndActionProvider() {
    return Stream.of(
        arguments("user", "add"),
        arguments("user", "delete"),
        arguments("user", "modify"),
        arguments("user", "save"),
        arguments("admin", "detail"));
  }

  @BeforeEach
  void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    final String TEST_TITLE = "test title";
    final long TEST_CREATED = 1000000;
    final long TEST_LAST_MODIFIED = 2000000;
    final String TEST_AUTHOR = "test author";
    final String TEST_DETAIL = "test detail";
    this.testNews =
        new News(TEST_TITLE, TEST_CREATED, TEST_LAST_MODIFIED, TEST_AUTHOR, TEST_DETAIL);
    assertTrue(this.newsDao.insertNews(this.testNews));
    List<News> newsList = this.newsDao.listNews(20);
    for (News news : newsList) {
      if (TEST_TITLE.equals(news.getTitle())) {
        this.testNews.setId(news.getId());
        break;
      }
    }
  }

  @AfterEach
  void restore() {
    assertTrue(this.newsDao.deleteNewsById(this.testNews.getId()));
  }

  @ParameterizedTest
  @MethodSource("illegalUserTypeAndActionProvider")
  void givenUserTypeWhenActionIsIllegalThenReturnToBlank(String userType, String action)
      throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", userType);
    final String viewName;
    if ("admin".equals(userType)) {
      viewName = "mainAdmin";
    } else {
      viewName = "mainUser";
    }
    this.mockMvc
        .perform(get("/news?action=" + action).sessionAttrs(sessionAttrs))
        .andExpect(status().isOk())
        .andExpect(view().name(viewName))
        .andExpect(forwardedUrl(this.urlPrefix + viewName + ".jsp"))
        .andExpect(model().attribute("mainPage", userType + "/news.jsp"))
        .andReturn();
  }

  @Test
  void givenUserWhenActionIsDetailThenRedirectToNewsDetail() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "user");
    MvcResult result =
        this.mockMvc
            .perform(
                get("/news?action=detail&newsId=" + this.testNews.getId())
                    .sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainUser"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainUser.jsp"))
            .andExpect(model().attribute("mainPage", "user/newsDetail.jsp"))
            .andReturn();
    News news = (News) result.getModelAndView().getModelMap().get("news");
    assertAll(() -> assertEquals(this.testNews, news));
  }

  @Test
  void givenUserWhenActionIsListThenList() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "user");
    MvcResult result =
        this.mockMvc
            .perform(get("/news?action=list").sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainUser"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainUser.jsp"))
            .andExpect(model().attribute("mainPage", "user/news.jsp"))
            .andReturn();
    List<News> newsList = (List<News>) result.getModelAndView().getModelMap().get("newsList");
    assertAll(
        () -> assertEquals(1, newsList.size()), () -> assertEquals(this.testNews, newsList.get(0)));
  }

  @Test
  void givenAdminWhenActionIsAddThenRedirectToNewsModify() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result =
        this.mockMvc
            .perform(get("/news?action=add").sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/newsModify.jsp"))
            .andReturn();
    assertAll(() -> assertNull(result.getModelAndView().getModelMap().get("news")));
  }

  @Test
  void givenAdminWhenActionIsModifyThenRedirectToNewsModify() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result =
        this.mockMvc
            .perform(
                get("/news?action=modify&newsId=" + this.testNews.getId())
                    .sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/newsModify.jsp"))
            .andReturn();
    assertAll(
        () -> assertEquals(this.testNews, result.getModelAndView().getModelMap().get("news")));
  }

  @Test
  void givenAdminWhenActionIsDeleteThenDelete() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result =
        this.mockMvc
            .perform(
                get("/news?action=delete&newsId=" + this.testNews.getId())
                    .sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/news.jsp"))
            .andReturn();
    List<News> newsList = (List<News>) result.getModelAndView().getModelMap().get("newsList");
    assertAll(() -> assertEquals(0, newsList.size()));
  }

  @Test
  void givenAdminWhenActionIsSaveNewNewsThenSave() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    final String TEST_TITLE = "test new title";
    final String TEST_AUTHOR = "test new author";
    final String TEST_DETAIL = "test new detail";
    MvcResult result =
        this.mockMvc
            .perform(
                post("/news?action=save")
                    .sessionAttrs(sessionAttrs)
                    .param("newsId", "")
                    .param("title", TEST_TITLE)
                    .param("author", TEST_AUTHOR)
                    .param("detail", TEST_DETAIL))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/news.jsp"))
            .andReturn();
    List<News> newsList = (List<News>) result.getModelAndView().getModelMap().get("newsList");
    assertAll(
        () -> assertEquals(2, newsList.size()),
        () -> assertEquals(this.testNews, newsList.get(0)),
        () -> assertEquals(TEST_TITLE, newsList.get(1).getTitle()),
        () -> assertEquals(TEST_AUTHOR, newsList.get(1).getAuthor()),
        () -> assertEquals(TEST_DETAIL, newsList.get(1).getDetail()),
        // Delete added data from database
        () -> assertTrue(this.newsDao.deleteNewsById(newsList.get(1).getId())));
  }

  @Test
  void givenAdminWhenActionIsSaveModifiedNewsThenSave() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    final String TEST_TITLE = "test new title";
    final String TEST_AUTHOR = "test new author";
    final String TEST_DETAIL = "test new detail";
    MvcResult result =
        this.mockMvc
            .perform(
                post("/news?action=save")
                    .sessionAttrs(sessionAttrs)
                    .param("newsId", String.valueOf(this.testNews.getId()))
                    .param("title", TEST_TITLE)
                    .param("author", TEST_AUTHOR)
                    .param("detail", TEST_DETAIL))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/news.jsp"))
            .andReturn();
    List<News> newsList = (List<News>) result.getModelAndView().getModelMap().get("newsList");
    assertAll(
        () -> assertEquals(1, newsList.size()),
        () -> assertEquals(TEST_TITLE, newsList.get(0).getTitle()),
        () -> assertEquals(TEST_AUTHOR, newsList.get(0).getAuthor()),
        () -> assertEquals(TEST_DETAIL, newsList.get(0).getDetail()));
  }

  @Test
  void givenAdminWhenActionIsListThenList() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result =
        this.mockMvc
            .perform(get("/news?action=list").sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(this.urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/news.jsp"))
            .andReturn();
    List<News> newsList = (List<News>) result.getModelAndView().getModelMap().get("newsList");
    assertAll(
        () -> assertEquals(1, newsList.size()), () -> assertEquals(this.testNews, newsList.get(0)));
  }
}
