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

    List<User> users;
    Map<String, User> userMap;

    static Stream<Arguments> userProvider() {
        return Stream.of(
                Arguments.of("username1", "password1", "name1", "MALE", "tel1"),
                Arguments.of("username2", "password2", "name2", "FEMALE", "tel2"),
                Arguments.of("username3", "password3", "name3", "MALE", "tel3"),
                Arguments.of("username4", "password4", "name4", "FEMALE", "tel4"),
                Arguments.of("username5", "password5", "name5", "MALE", "tel5")
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
        this.users = this.userDao.queryAllUsers();
        this.userMap = new HashMap<>();
        for (User user : this.users) {
            this.userMap.put(user.getUsername(), user);
        }
    }

    @AfterEach
    void tearDown() {
        for (User user : this.users) {
            this.userDao.deleteUser(user.getId());
        }
    }

    @Test
    void shouldReturnListUser_whenAdminActionList() throws Throwable {
        List<String> usernames = Arrays.asList("username5", "username4", "username3", "username2", "username1");
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
        List<String> usernames = Arrays.asList("username5", "username4", "username3", "username2", "username1");
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
        User expectedUser = this.userMap.get(username);
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
    @MethodSource("userProvider")
    void updateUsers(String username, String password, String name, String sex, String tel) throws Throwable {
        User addedUser = this.userMap.get(username);
        int id = addedUser.getId();
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
        User updatedUser = this.userDao.queryUserByName(name).get(0);
        assertAll(
                () -> assertEquals(username, updatedUser.getUsername()),
                () -> assertEquals(newPassword, updatedUser.getPassword()),
                () -> assertEquals(name, updatedUser.getName()),
                () -> assertEquals(sex, updatedUser.getSex().toString()),
                () -> assertEquals(tel, updatedUser.getTel())
        );
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
