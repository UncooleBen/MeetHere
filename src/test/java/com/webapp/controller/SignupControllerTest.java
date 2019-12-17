package com.webapp.controller;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class SignupControllerTest
{
    private LoginDao loginDao=mock(LoginDao.class);
    private SignupController signupController=new SignupController(loginDao);
    private HttpServletRequest request=mock(HttpServletRequest.class);
    private HttpSession session=mock(HttpSession.class);
    private HttpServletResponse response=mock(HttpServletResponse.class);


    @Test
    public  void signUpPage_test()
    {
        ModelAndView result= signupController.signupPage();
        assertEquals(result.getViewName(),"signup");

    }

    @Test
    public  void signUpSubmit_WhenUserIsNull()throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("PengGe");
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getParameter("name")).thenReturn("PengJunTao");
        when(request.getParameter("tel")).thenReturn("911");
        when(request.getParameter("sex")).thenReturn("FEMALE");

        User user = new User("PengGe", "123456", "PengJunTao", "FEMALE", "911");

        User currentUser = null;


        when(loginDao.signup(user)).thenReturn(currentUser);


        ModelAndView result = signupController.signupSubmit(request, response);

        verify(request).setCharacterEncoding("utf-8");
        assertAll(
                () -> assertEquals(result.getViewName(), "signup"),
                () -> assertEquals(result.getModelMap().get("error"), "该用户名已存在")
        );
    }

        @Test
        public  void signUpSubmit_WhenUserIsNotNull()throws ServletException, IOException
        {
            when(request.getParameter("username")).thenReturn("PengGe");
            when(request.getParameter("password")).thenReturn("123456");
            when(request.getParameter("name")).thenReturn("PengJunTao");
            when(request.getParameter("tel")).thenReturn("911");
            when(request.getParameter("sex")).thenReturn("FEMALE");

            User user=new User("PengGe","123456","PengJunTao","FEMALE","911");

            User currentUser=new User();


            when(loginDao.signup(user)).thenReturn(currentUser);



            ModelAndView result= signupController.signupSubmit(request,response);

            assertAll(
                    ()->assertEquals(result.getViewName(),"index"),
                    ()->assertEquals(result.getModelMap().get("user"),currentUser)
            );
        }
}