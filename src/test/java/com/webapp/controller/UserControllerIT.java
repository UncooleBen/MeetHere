package com.webapp.controller;

import com.webapp.config.MvcConfig;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.UserDao;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Juntao Peng
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfig.class)
public class UserControllerIT {
    @Autowired
    UserDao userDao;
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    static Stream<Arguments> userProvider() {
        return Stream.of(
                Arguments.of("username1", "password1", "name1", "MALE", "tel1"),
                Arguments.of("username2", "password2", "name2", "FEMALE", "tel2"),
                Arguments.of("username3", "password3", "name3", "MALE", "tel3"),
                Arguments.of("username4", "password4", "name4", "FEMALE", "tel4"),
                Arguments.of("username5", "password5", "name5", "MALE", "tel5")
        );
    }

    static Stream<Arguments> existedUserProvider() {
        return Stream.of(
                Arguments.of(9, "testuser", "test", "testusername", "MALE", "testtel"),
                Arguments.of(8, "gyj", "gyj", "guoyuanjie", "MALE", "123123"),
                Arguments.of(7, "lsz", "lsz", "lishanzhen", "MALE", "123123")
        );
    }

    static Stream<Integer> idProvider() {
        return Stream.of(1, 2, 3, 4, 5);
    }

    static Stream<String> sexProvider() {
        return Stream.of("FEMALE", "MALE");
    }

    static Stream<String> nameProvider() {
        return Stream.of("pengjuntao", "lishangzhen", "guoyuanjie", "paul.n.hilfinger");
    }

    static Stream<Integer> searchIdProvider() {
        return Stream.of(4, 7, 8, 999);
    }

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void shouldReturnListUser_whenAdminActionList() throws Throwable {
        List<String> usernames = Arrays.asList("999", "testuser", "gyj", "lsz", "pjt");
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(get("/user?action=list").sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/user.jsp"))
                .andReturn();
        List<User> resultUsers = (List<User>) result.getModelAndView().getModelMap().get("userList");
        for (int i = 0; i < usernames.size(); i++) {
            String expected = usernames.get(i);
            String actual = resultUsers.get(i).getUsername();
            assertEquals(expected, actual);
        }
    }

    @Test
    void shouldReturnListUser_whenAdminActionNull() throws Throwable {
        List<String> usernames = Arrays.asList("999", "testuser", "gyj", "lsz", "pjt");
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(get("/user?action=").sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/user.jsp"))
                .andReturn();
        List<User> resultUsers = (List<User>) result.getModelAndView().getModelMap().get("userList");
        for (int i = 0; i < usernames.size(); i++) {
            String expected = usernames.get(i);
            String actual = resultUsers.get(i).getUsername();
            assertEquals(expected, actual);
        }
    }

    @ParameterizedTest
    @MethodSource("userProvider")
    void deleteUsers(String username, String password, String name, String sex, String tel) throws Throwable {
        User expectedUser = new User(username, password, name, sex, tel);
        this.userDao.addUser(expectedUser);
        expectedUser = this.userDao.queryAllUsers().get(0);
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(get("/user?action=delete&id=" + expectedUser.getId()).sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/user.jsp"))
                .andReturn();
        List<User> resultUsers = (List<User>) result.getModelAndView().getModelMap().get("userList");
        for (int i = 0; i < resultUsers.size(); i++) {
            String expected = resultUsers.get(i).getUsername();
            assertNotEquals(expected, username);
        }
    }

    @ParameterizedTest
    @MethodSource("userProvider")
    void saveUsersThenDelete(String username, String password, String name, String sex, String tel) throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        this.mockMvc.perform(post("/user?action=save")
                .param("username", username)
                .param("password", password)
                .param("name", name)
                .param("sex", sex)
                .param("tel", tel)
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/user.jsp"));
        User addedUser = this.userDao.queryAllUsers().get(0);
        assertAll(
                () -> assertEquals(username, addedUser.getUsername()),
                () -> assertEquals(password, addedUser.getPassword()),
                () -> assertEquals(name, addedUser.getName()),
                () -> assertEquals(sex, addedUser.getSex().toString()),
                () -> assertEquals(tel, addedUser.getTel())
        );
        this.userDao.deleteUser(addedUser.getId());
    }

    @ParameterizedTest
    @MethodSource("existedUserProvider")
    void updateUsersThenRestore(int id, String username, String password, String name, String sex, String tel) throws Throwable {
        String newPassword = "clandestine password";
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        this.mockMvc.perform(post("/user?action=save")
                .param("username", username)
                .param("password", newPassword)
                .param("name", name)
                .param("sex", sex)
                .param("tel", tel)
                .param("id", String.valueOf(id))
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/user.jsp"));
        User addedUser = this.userDao.queryUserByName(name).get(0);
        assertAll(
                () -> assertEquals(username, addedUser.getUsername()),
                () -> assertEquals(newPassword, addedUser.getPassword()),
                () -> assertEquals(name, addedUser.getName()),
                () -> assertEquals(sex, addedUser.getSex().toString()),
                () -> assertEquals(tel, addedUser.getTel())
        );
        // Restore password
        this.userDao.updateUserPassword(id, password);
    }

    @ParameterizedTest
    @MethodSource("idProvider")
    void shouldForwardToModify_whenActionIsModify(int id) throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        this.mockMvc.perform(post("/user?action=modify&id=" + id)
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/userModify.jsp"))
                .andExpect(model().attribute("id", String.valueOf(id)));
    }

    @Test
    void shouldForwardToModify_whenActionIsAdd() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        this.mockMvc.perform(post("/user?action=add")
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/userModify.jsp"));
    }

    @ParameterizedTest
    @MethodSource("sexProvider")
    void shouldReturnUserofSearchedSex(String sex) throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(post("/user?action=search")
                .sessionAttrs(sessionAttrs)
                .param("searchType", "sex")
                .param("searchUser_text", sex))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/user.jsp"))
                .andReturn();
        List<User> resultUsers = (List<User>) result.getModelAndView().getModelMap().get("userList");
        for (User user : resultUsers) {
            assertEquals(sex, user.getSex().toString());
        }
    }

    @ParameterizedTest
    @MethodSource("nameProvider")
    void shouldReturnUserofSearchedName(String name) throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(post("/user?action=search")
                .sessionAttrs(sessionAttrs)
                .param("searchType", "name")
                .param("searchUser_text", name))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/user.jsp"))
                .andReturn();
        List<User> resultUsers = (List<User>) result.getModelAndView().getModelMap().get("userList");
        for (User user : resultUsers) {
            assertEquals(name, user.getName());
        }
    }

    @ParameterizedTest
    @MethodSource("searchIdProvider")
    void shouldReturnUserofSearchedId(int id) throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(post("/user?action=search")
                .sessionAttrs(sessionAttrs)
                .param("searchType", "id")
                .param("searchUser_text", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/user.jsp"))
                .andReturn();
        List<User> resultUsers = (List<User>) result.getModelAndView().getModelMap().get("userList");
        for (User user : resultUsers) {
            assertEquals(id, user.getId());
        }
    }

    @Test
    void userShouldNotBeServedByUserController() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "user");
        this.mockMvc.perform(post("/user?action=search")
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainUser"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainUser.jsp"));
    }
}
