package com.webapp.controller;

import com.webapp.model.user.User;
import com.webapp.service.database.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordControllerTest {

    private UserDao userDao=mock(UserDao.class);
    private PasswordController passwordController=new PasswordController(userDao);

    private HttpServletRequest request=mock(HttpServletRequest.class);
    private HttpSession session=mock(HttpSession.class);

    @Test
    public  void service_WhenUserIsUser_ActionIsChange()
    {
        String action="change";
        String usertype="user";


        when(session.getAttribute("currentUserType")).thenReturn(usertype);
        when(request.getSession()).thenReturn(session);
        ModelAndView result=passwordController.service(action,request);

        assertAll(
                ()->assertEquals(result.getViewName(),"mainUser"),
                ()->assertEquals(result.getModelMap().get("mainPage"),"passwordChange.jsp")
        );

    }

    @Test
    public  void service_WhenUserIsUser_ActionIsSave()
        {
        String action="save";
        String usertype="user";

        User user=new User();
        user.setId(305);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(user);
        when(request.getParameter("newPassword")).thenReturn("123");
        ModelAndView result=passwordController.service(action,request);

        verify(userDao).updateUserPassword(305,"123");
        assertAll(
                ()->assertEquals(result.getViewName(),"mainUser")

        );

    }

    @Test
    public  void service_WhenUserIsAdmin_ActionIsChange()
    {
        String action="change";
        String usertype="admin";


        when(session.getAttribute("currentUserType")).thenReturn(usertype);
        when(request.getSession()).thenReturn(session);
        ModelAndView result=passwordController.service(action,request);

        assertAll(
                ()->assertEquals(result.getViewName(),"mainAdmin"),
                ()->assertEquals(result.getModelMap().get("mainPage"),"passwordChange.jsp")
        );

    }


    @Test
    public  void service_WhenUserIsAdmin_ActionIsSave()
    {
        String action="save";
        String usertype="admin";

        User user=new User();
        user.setId(305);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(user);
        when(request.getParameter("newPassword")).thenReturn("123");
        ModelAndView result=passwordController.service(action,request);

        verify(userDao).updateUserPassword(305,"123");

        assertAll(
                ()->assertEquals(result.getViewName(),"mainAdmin")
        );

    }
}