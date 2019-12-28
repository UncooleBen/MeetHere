package com.webapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.webapp.config.MvcConfig;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.UserDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Shangzhen Li
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfig.class)
public class PasswordControllerIT {

  final String urlPrefix = "/WEB-INF/jsp/";
  MockMvc mockMvc;
  @Autowired
  UserDao userDao;
  @Autowired
  WebApplicationContext wac;
  User testUser;

  @BeforeEach
  void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    final String TEST_USERNAME = "testUsername";
    final String TEST_OLD_PASSWORD = "testOldPassword";
    final String TEST_NAME = "testName";
    final String TEST_GENDER = "MALE";
    final String TEST_TEL = "testTel";
    this.testUser = new User(TEST_USERNAME, TEST_OLD_PASSWORD, TEST_NAME, TEST_GENDER, TEST_TEL);
    assertTrue(this.userDao.addUser(this.testUser));
    List<User> userList = this.userDao.queryUserByName(TEST_NAME);
    assertEquals(1, userList.size());
    this.testUser = userList.get(0);
  }

  @AfterEach
  void restore() {
    assertTrue(this.userDao.deleteUser(this.testUser.getId()));
  }

  @ParameterizedTest
  @ValueSource(strings = {"user", "admin"})
  void whenIllegalActionFoundThenReturnToBlank(String userType) throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", userType);
    sessionAttrs.put("currentUser", this.testUser);
    final String viewName;
    if ("admin".equals(userType)) {
      viewName = "mainAdmin";
    } else {
      viewName = "mainUser";
    }
    this.mockMvc
        .perform(post("/password?action=other").sessionAttrs(sessionAttrs))
        .andExpect(status().isOk())
        .andExpect(view().name(viewName))
        .andExpect(forwardedUrl(this.urlPrefix + viewName + ".jsp"))
        /*mainPage is null*/
        .andReturn();
  }

  @ParameterizedTest
  @ValueSource(strings = {"user", "admin"})
  void whenActionIsChangeThenRedirectToPasswordChange(String userType) throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", userType);
    sessionAttrs.put("currentUser", this.testUser);
    final String viewName;
    if ("admin".equals(userType)) {
      viewName = "mainAdmin";
    } else {
      viewName = "mainUser";
    }
    this.mockMvc
        .perform(post("/password?action=change").sessionAttrs(sessionAttrs))
        .andExpect(status().isOk())
        .andExpect(view().name(viewName))
        .andExpect(forwardedUrl(this.urlPrefix + viewName + ".jsp"))
        .andExpect(model().attribute("mainPage", "passwordChange.jsp"))
        .andReturn();
  }

  @ParameterizedTest
  @ValueSource(strings = {"user", "admin"})
  void whenActionIsSaveThenSave(String userType) throws Exception {
    final String TEST_NEW_PASSWORD = "testNewPassword";
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", userType);
    sessionAttrs.put("currentUser", this.testUser);
    final String viewName;
    if ("admin".equals(userType)) {
      viewName = "mainAdmin";
    } else {
      viewName = "mainUser";
    }
    this.mockMvc
        .perform(
            post("/password?action=save")
                .sessionAttrs(sessionAttrs)
                .param("userId", String.valueOf(this.testUser.getId()))
                .param("oldPassword", this.testUser.getPassword())
                .param("newPassword", TEST_NEW_PASSWORD))
        .andExpect(status().isOk())
        .andExpect(view().name(viewName))
        .andExpect(forwardedUrl(this.urlPrefix + viewName + ".jsp"))
        /*mainPage is null*/
        .andReturn();
    this.testUser = this.userDao.queryUserById(this.testUser.getId());
    assertEquals(TEST_NEW_PASSWORD, this.testUser.getPassword());
  }
}
