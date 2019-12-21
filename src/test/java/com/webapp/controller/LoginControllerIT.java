package com.webapp.controller;

import com.webapp.config.MvcConfig;
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

import java.util.Arrays;
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

    static Stream<String> errorUsernameProvider() {
        return Stream.of("1024", "is", "an", "invalid", "website");
    }

    static Stream<Arguments> errorUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("pjt", "'s password is not pjt"),
                Arguments.of("lsz", "'s password is not lsz"),
                Arguments.of("gyj", "'s password is not gyj")
        );
    }

    static Stream<Arguments> errorAdminUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("root", "'s password is not root"),
                Arguments.of("admin", "'s password is not admin"),
                Arguments.of("chairman", "'s password is not chairman")
        );
    }

    static Stream<Arguments> correctUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("pjt", "pjt"),
                Arguments.of("lsz", "lsz"),
                Arguments.of("gyj", "gyj")
        );
    }

    static Stream<Arguments> correctAdminUsernamePasswordPairProvider() {
        return Stream.of(
                Arguments.of("root", "root"),
                Arguments.of("admin", "admin"),
                Arguments.of("chairman", "chairman")
        );
    }

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @AfterEach
    void tearDown() {

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
