package com.webapp.controller;

import com.webapp.config.MvcConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author Juntao Peng
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfig.class)
public class BlankControllerIT {
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void shouldForwardToBlankJsp_WhenLoginAsUser_AndRequestIsBlank() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "user");
        this.mockMvc.perform(get("/blank").sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainUser"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainUser.jsp"))
                .andExpect(model().attribute("mainPage", "user/blank.jsp"));
    }

    @Test
    void shouldForwardToBlankJsp_WhenLoginAsAdmin_AndRequestIsBlank() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        this.mockMvc.perform(get("/blank").sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/blank.jsp"));
    }

    @Test
    void shouldForwardToIndex_WhenNotLogin_AndRequestIsBlank() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "");
        this.mockMvc.perform(get("/blank").sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"));
    }
}
