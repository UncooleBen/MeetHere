package com.webapp.controller;

import com.webapp.config.MvcConfig;
import com.webapp.model.user.Gender;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.UserDao;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.Arrays;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Juntao Peng
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfig.class)
public class SignupControllerIT {
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Autowired
    UserDao userDao;

    User addedUser;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        User userToAdd = new User("999", "999", "999", "MALE", "13918300851");
        this.userDao.addUser(userToAdd);
        this.addedUser = this.userDao.queryAllUsers().get(0);
    }

    @AfterEach
    void tearDown() {
        this.userDao.deleteUser(this.addedUser.getId());
    }

    @Test
    void shouldGoToSignupPage_WhenRequestIsSignup() throws Throwable {
        this.mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/signup.jsp"));
    }

    static Stream<Arguments> validUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("username1", "password1", "name1", "MALE", "tel1"),
                Arguments.of("username2", "password2", "name2", "FEMALE", "tel2"),
                Arguments.of("username3", "password3", "name3", "MALE", "tel3"),
                Arguments.of("username4", "password4", "name4", "FEMALE", "tel4"),
                Arguments.of("username5", "password5", "name5", "MALE", "tel5")
        );
    }

    @ParameterizedTest
    @MethodSource("validUsernamePasswordPairProvider")
    void shouldGoToIndexWithUsernameFilled_WhenSignupSucceeded(String username, String password, String name,
                                                               String sex, String tel) throws Throwable {
        this.mockMvc.perform(post("/signupSubmit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", username),
                        new BasicNameValuePair("password", password),
                        new BasicNameValuePair("name", name),
                        new BasicNameValuePair("sex", sex),
                        new BasicNameValuePair("tel", tel)
                )))))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"))
                .andExpect(model().attribute("user", hasProperty("username", is(username))))
                .andExpect(model().attribute("user", hasProperty("password", is(password))))
                .andExpect(model().attribute("user", hasProperty("name", is(name))))
                .andExpect(model().attribute("user", hasProperty("sex", is(Gender.valueOf(sex)))))
                .andExpect(model().attribute("user", hasProperty("tel", is(tel))));
        // Delete the added user
        User userAdded = this.userDao.queryUserByName(name).get(0);
        this.userDao.deleteUser(userAdded.getId());
    }

    static Stream<Arguments> invalidUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("999", "999", "999", "MALE", "13918300851"),
                Arguments.of("999", "---", "999", "MALE", "13918300851"),
                Arguments.of("999", "999", "---", "MALE", "13918300851"),
                Arguments.of("999", "999", "999", "FEMALE", "13918300851"),
                Arguments.of("999", "999", "999", "MALE", "---")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUsernamePasswordPairProvider")
    void shouldErrorMessage_WhenSignupFailed(String username, String password, String name,
                                             String sex, String tel) throws Throwable {
        this.mockMvc.perform(post("/signupSubmit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", username),
                        new BasicNameValuePair("password", password),
                        new BasicNameValuePair("name", name),
                        new BasicNameValuePair("sex", sex),
                        new BasicNameValuePair("tel", tel)
                )))))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/signup.jsp"))
                .andExpect(model().attribute("error", "该用户名已存在"));
    }
}
