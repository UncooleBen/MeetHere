package com.webapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Guoyuanjie
 */

class BlankControllerTest {
    @Test
    public void service_RequestIsAdmin_ReturnAdminBlank() throws ServletException, IOException {
        BlankController blankcontroller = new BlankController();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Object type = "admin";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(type);
        ModelAndView result = blankcontroller.service(request, response);

        assertAll(
                () -> assertEquals("mainAdmin",result.getViewName()),
                () -> assertEquals("admin/blank.jsp",result.getModelMap().get("mainPage") )
        );
    }

    @Test
    public void service_RequestIsUser_ReturnUserBlank() throws ServletException, IOException {
        BlankController blankcontroller = new BlankController();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Object type = "user";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(type);
        ModelAndView result = blankcontroller.service(request, response);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainUser"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "user/blank.jsp")
        );
    }

    @Test
    public void service_RequestIsOther_ReturnAdminBlank() throws ServletException, IOException {
        BlankController blankcontroller = new BlankController();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Object type = "other";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(type);
        ModelAndView result = blankcontroller.service(request, response);


        assertEquals(result.getViewName(), "index");

    }
}