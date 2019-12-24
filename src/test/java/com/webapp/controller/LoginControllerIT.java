package com.webapp.controller;

import com.webapp.config.MvcConfig;
import com.webapp.model.user.Admin;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.UserDao;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Juntao Peng
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfig.class)
public class LoginControllerIT {
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Autowired
    UserDao userDao;

    List<User> users;

    static Stream<String> errorUsernameProvider() {
        return Stream.of("1024", "is", "an", "invalid", "website");
    }

    static Stream<Arguments> errorUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("username1", "password1-"),
                Arguments.of("username2", "passwo-rd2"),
                Arguments.of("username3", "passw-ord3"),
                Arguments.of("username4", "passwor-d4"),
                Arguments.of("username5", "passw-ord5")
        );
    }

    static Stream<Arguments> errorAdminUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("admin1", "password1-"),
                Arguments.of("admin2", "password2-"),
                Arguments.of("admin3", "password3-")
        );
    }

    static Stream<Arguments> correctUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("username1", "password1"),
                Arguments.of("username2", "password2"),
                Arguments.of("username3", "password3"),
                Arguments.of("username4", "password4"),
                Arguments.of("username5", "password5")
        );
    }

    static Stream<Arguments> correctAdminUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("admin1", "password1"),
                Arguments.of("admin2", "password2"),
                Arguments.of("admin3", "password3")
        );
    }

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        User user1 = new User("username1", "password1", "name1", "MALE", "tel1");
        User user2 = new User("username2", "password2", "name2", "FEMALE", "tel2");
        User user3 = new User("username3", "password3", "name3", "MALE", "tel3");
        User user4 = new User("username4", "password4", "name4", "FEMALE", "tel4");
        User user5 = new User("username5", "password5", "name5", "MALE", "tel5");
        this.userDao.addUser(user1);
        this.userDao.addUser(user2);
        this.userDao.addUser(user3);
        this.userDao.addUser(user4);
        this.userDao.addUser(user5);
        Admin admin1 = new Admin("admin1", "password1", "name1", "MALE", "tel1");
        Admin admin2 = new Admin("admin2", "password2", "name2", "MALE", "tel2");
        Admin admin3 = new Admin("admin3", "password3", "name3", "MALE", "tel3");
        this.userDao.addUser(admin1);
        this.userDao.addUser(admin2);
        this.userDao.addUser(admin3);
        this.users = this.userDao.queryAllUsers();
    }

    @AfterEach
    void tearDown() {
        for (User user : this.users) {
            this.userDao.deleteUser(user.getId());
        }
    }

    @ParameterizedTest
    @MethodSource("errorUsernameProvider")
    void shouldPromptErrorMessage_WhenUsernameError(String username) throws Throwable {
        String password = "clandestine password";
        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", username),
                        new BasicNameValuePair("password", password)
                )))))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"))
                .andExpect(model().attribute("error", "用户名或密码错误"));
    }

    @ParameterizedTest
    @MethodSource("errorUsernamePasswordPairProvider")
    void shouldPromptErrorMessage_WhenPasswordError_OnUserType(String username, String password) throws Throwable {
        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", username),
                        new BasicNameValuePair("password", password)
                )))))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"))
                .andExpect(model().attribute("error", "用户名或密码错误"));
    }

    @ParameterizedTest
    @MethodSource("errorAdminUsernamePasswordPairProvider")
    void shouldPromptErrorMessage_WhenPasswordError_OnAdminType(String username, String password) throws Throwable {
        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", username),
                        new BasicNameValuePair("password", password)
                )))))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"))
                .andExpect(model().attribute("error", "用户名或密码错误"));
    }

    @ParameterizedTest
    @MethodSource("correctUsernamePasswordPairProvider")
    void shouldForwardToUserBlank_WhenPasswordCorrect_OnUserType(String username, String password) throws Throwable {
        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", username),
                        new BasicNameValuePair("password", password)
                )))))
                .andExpect(status().isOk())
                .andExpect(view().name("mainUser"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainUser.jsp"))
                .andExpect(model().attribute("mainPage", "user/blank.jsp"));
    }

    @ParameterizedTest
    @MethodSource("correctAdminUsernamePasswordPairProvider")
    void shouldForwardToAdminBlank_WhenPasswordCorrect_OnAdminType(String username, String password) throws Throwable {
        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", username),
                        new BasicNameValuePair("password", password)
                )))))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/blank.jsp"));
    }

}
