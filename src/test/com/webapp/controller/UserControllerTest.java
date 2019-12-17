package com.webapp.controller;

import com.webapp.service.database.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

import com.webapp.model.user.User;
import java.util.ArrayList;
import java.util.List;

class UserControllerTest {

    private UserDao userDao=mock(UserDao.class);
    private HttpServletRequest request=mock(HttpServletRequest.class);
    private HttpSession session=mock(HttpSession.class);


    private UserController userController=new UserController(userDao);


    @Test
    public void service_When_IsNotAuthorized_ThenReturn_mv()
    {
        String action="";
        String type="";
        when(session.getAttribute("currentUserType")).thenReturn(type);
        ModelAndView result=userController.service(action,request,session);
        assertNotNull(result);

    }

    @Test
    public void service_When_ActionIsDelete()
    {
        String action="delete";
        String type="admin";

        List<User>  users=new ArrayList<User>();
        users.add(new User());

        when(session.getAttribute("currentUserType")).thenReturn(type);
        when(request.getParameter("id")).thenReturn("305");
        when(userDao.queryAllUsers()).thenReturn(users);





        ModelAndView result=userController.service(action,request,session);

        assertAll(
                ()->assertEquals(result.getViewName(),"mainAdmin"),
                ()->assertEquals(result.getModelMap().get("mainPage"),"admin/user.jsp"),
                ()->assertEquals(result.getModelMap().get("userList"),users)
        );

        verify(userDao).deleteUser(305);
    }

    @Test
    public void service_When_ActionIsModify()
    {
        String action="modify";
        String type="admin";

        User user=new User();

        when(session.getAttribute("currentUserType")).thenReturn(type);
        when(request.getParameter("id")).thenReturn("305");


        when(userDao.queryUserById(305)).thenReturn(user);



        ModelAndView result=userController.service(action,request,session);

        assertEquals(result.getViewName(),"mainAdmin");
        assertEquals(result.getModelMap().get("mainPage"),"admin/userModify.jsp");
        assertEquals(result.getModelMap().get("id"),String.valueOf(305));
        assertEquals(result.getModelMap().get("user"),user);

    }

    @Test
    public void service_When_ActionIsAdd()
    {
        String action="add";
        String type="admin";


        when(session.getAttribute("currentUserType")).thenReturn(type);
        when(request.getParameter("id")).thenReturn("305");




        ModelAndView result=userController.service(action,request,session);

        assertEquals(result.getViewName(),"mainAdmin");
        assertEquals(result.getModelMap().get("mainPage"),"admin/userModify.jsp");
        assertEquals(result.getModelMap().get("id"),null);
        assertEquals(result.getModelMap().get("user"),null);

    }

    @Test
    public void service_When_ActionIsSave()
    {
        String action="save";
        String type="dsa";

        List<User>  users=new ArrayList<User>();
        users.add(new User());

        when(session.getAttribute("currentUserType")).thenReturn(type);
        when(request.getParameter("id")).thenReturn("305");
        when(userDao.queryAllUsers()).thenReturn(users);

        when(request.getParameter("username")).thenReturn("PengGe");
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getParameter("name")).thenReturn("PengJunTao");
        when(request.getParameter("tel")).thenReturn("911");
        when(request.getParameter("sex")).thenReturn("man");


        ModelAndView result=userController.service(action,request,session);
        verify(userDao.deleteUser(350));
        verify(userDao.queryAllUsers());
        assertEquals(result.getViewName(),"mainAdmin");
        assertEquals(result.getModelMap().get("mainPage"),"admin/user.jsp");

    }


    @Test
    public void service_When_ActionIsSearch()
    {
        String action="search";
        String type="dsa";

        List<User>  users=new ArrayList<User>();
        users.add(new User());

        when(session.getAttribute("currentUserType")).thenReturn(type);
        when(request.getParameter("id")).thenReturn("305");
        when(userDao.queryAllUsers()).thenReturn(users);



        ModelAndView result=userController.service(action,request,session);
        verify(userDao.deleteUser(350));
        verify(userDao.queryAllUsers());
        assertEquals(result.getViewName(),"mainAdmin");
        assertEquals(result.getModelMap().get("mainPage"),"admin/user.jsp");

    }

    @Test
    public void service_When_ActionIsList()
    {
        String action="list";
        String type="admin";

        List<User>  users=new ArrayList<User>();
        users.add(new User());

        when(session.getAttribute("currentUserType")).thenReturn(type);
        when(request.getParameter("id")).thenReturn("305");
        when(userDao.queryAllUsers()).thenReturn(users);





        ModelAndView result=userController.service(action,request,session);

        assertAll(
                ()->assertEquals(result.getViewName(),"mainAdmin"),
                ()->assertEquals(result.getModelMap().get("mainPage"),"admin/user.jsp"),
                ()->assertEquals(result.getModelMap().get("userList"),users)
        );

    }

    @Test
    public void service_When_ActionIsOther()
    {
        String action="other";
        String type="admin";

        List<User>  users=new ArrayList<User>();
        users.add(new User());

        when(session.getAttribute("currentUserType")).thenReturn(type);
        when(request.getParameter("id")).thenReturn("305");
        when(userDao.queryAllUsers()).thenReturn(users);





        ModelAndView result=userController.service(action,request,session);

        assertAll(
                ()->assertEquals(result.getViewName(),"mainAdmin"),
                ()->assertEquals(result.getModelMap().get("mainPage"),"admin/user.jsp"),
                ()->assertEquals(result.getModelMap().get("userList"),users)
        );


    }

    @Test
    public void service_When_UsersIsEmpty()
    {
        String action="other";
        String type="admin";

        List<User>  users=new ArrayList<User>();

        when(session.getAttribute("currentUserType")).thenReturn(type);
        when(request.getParameter("id")).thenReturn("305");
        when(userDao.queryAllUsers()).thenReturn(users);





        ModelAndView result=userController.service(action,request,session);

        assertAll(
                ()->assertEquals(result.getViewName(),"mainAdmin"),
                ()->assertEquals(result.getModelMap().get("mainPage"),"admin/user.jsp"),
                ()->assertEquals(result.getModelMap().get("userList"),null)
        );


    }
}