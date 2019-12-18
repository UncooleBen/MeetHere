package com.webapp.controller;

import com.webapp.model.user.Admin;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.LoginDao;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {
    private LoginDao loginDao=mock(LoginDao.class);
    private LoginController loginController=new LoginController(loginDao);
    private HttpServletRequest request=mock(HttpServletRequest.class);
    private HttpSession session=mock(HttpSession.class);
    private HttpServletResponse response=mock(HttpServletResponse.class);

    @Test
    public void service_whenUserIsNull() throws ServletException, IOException{

        String username="pengge";
        String password="123456";

        User user=null;

        when( request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);

        when(loginDao.login(username,password)).thenReturn(user);


        ModelAndView result=loginController.service(request,response);
        verify(request).setCharacterEncoding("utf-8");

        assertAll(
                ()->assertEquals(result.getViewName(),"index"),
                ()->assertEquals(result.getModelMap().get("error"),"用户名或密码错误")
        );
    }

    @Test
    public void service_whenUserIsUser() throws ServletException, IOException{

        String username="pengge";
        String password="123456";

        User user=new User();

        when( request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);


        when(loginDao.login(username,password)).thenReturn(user);


        ModelAndView result=loginController.service(request,response);
        verify(request).setCharacterEncoding("utf-8");
        verify(session).setAttribute("currentUserType", "user");
        verify(session).setAttribute("currentUser", user);

        assertAll(
                ()->assertEquals(result.getViewName(),"mainUser"),
                ()->assertEquals(result.getModelMap().get("user"),user)
        );
    }

    @Test
    public void service_whenUserIsAdmin() throws ServletException, IOException{

        String username="pengge";
        String password="123456";

        User user=new Admin();
        Admin admin=(Admin) user;
        when( request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);

        when(loginDao.login(username,password)).thenReturn(user);


        ModelAndView result=loginController.service(request,response);
        verify(request).setCharacterEncoding("utf-8");
        verify(session).setAttribute("currentUserType", "admin");
        verify(session).setAttribute("currentUser", admin);

        assertAll(
                ()->assertEquals(result.getViewName(),"mainAdmin"),
                ()->assertEquals(result.getModelMap().get("admin"),admin)
        );
    }
}