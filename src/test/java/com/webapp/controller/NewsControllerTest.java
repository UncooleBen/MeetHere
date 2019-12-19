package com.webapp.controller;

import com.webapp.model.News;
import com.webapp.service.database.dao.NewsDao;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NewsControllerTest {
    private NewsDao newsDao = mock(NewsDao.class);
    private NewsController newsController = new NewsController((newsDao));

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpSession session = mock(HttpSession.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    public void service_WhenUserIsAdmin_ActionIsAdd() {
        String action = "add";
        String usertype = "admin";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);


        ModelAndView result = newsController.service(action, request);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin")
        );
    }

    @Test
    public void service_WhenUserIsAdmin_ActionIsDelete() {
        String action = "delete";
        String usertype = "admin";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(request.getParameter("newsId")).thenReturn("123");

        ModelAndView result = newsController.service(action, request);

        verify(newsDao).deleteNewsById(123);
        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/news.jsp")
        );
    }

    @Test
    public void service_WhenUserIsAdmin_ActionIsModify() {
        String action = "modify";
        String usertype = "admin";
        News news = new News();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(request.getParameter("newsId")).thenReturn("123");
        when(newsDao.queryNewsById(123)).thenReturn(news);


        ModelAndView result = newsController.service(action, request);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("news"), news),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/newsModify.jsp")

        );
    }

    @Test
    public void service_WhenUserIsAdmin_ActionIsSave_IdIsNotNull() {
        String action = "save";
        String usertype = "admin";

        News news = new News();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(request.getParameter("newsId")).thenReturn("123");
        when(newsDao.queryNewsById(123)).thenReturn(news);

        ModelAndView result = newsController.service(action, request);
        verify(newsDao).updateNews(news);
        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/news.jsp")
        );
    }

    @Test
    public void service_WhenUserIsAdmin_ActionIsSave_IdIsNull() {
        String action = "save";
        String usertype = "admin";

        News news = new News();
        Date time = new Date();


        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(request.getParameter("newsId")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("2");
        when(request.getParameter("author")).thenReturn("3");
        when(request.getParameter("detail")).thenReturn("4");
        //news.se
        //TODO

        ModelAndView result = newsController.service(action, request);
        verify(newsDao).insertNews(news);
        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/news.jsp")
        );
    }

    @Test
    public void service_WhenUserIsAdmin_ActionIsList() {
        String action = "list";
        String usertype = "admin";
        List<News> newsList = new LinkedList<News>();


        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);
        when(newsDao.listNews(20)).thenReturn(newsList);


        ModelAndView result = newsController.service(action, request);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/news.jsp"),
                () -> assertEquals(result.getModelMap().get("newsList"), newsList)
        );
    }

    @Test
    public void service_WhenUserIsAdmin_ActionIsOther() {
        String action = "other";
        String usertype = "admin";
        List<News> newsList = new LinkedList<News>();


        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);
        when(newsDao.listNews(20)).thenReturn(newsList);


        ModelAndView result = newsController.service(action, request);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/news.jsp"),
                () -> assertEquals(result.getModelMap().get("newsList"), newsList)
        );
    }

    @Test
    public void service_WhenUserIsUser_ActionIsDetail() {
        String action = "detail";
        String usertype = "user";

        News news = new News();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(request.getParameter("newsId")).thenReturn("123");
        when(newsDao.queryNewsById(123)).thenReturn(news);


        ModelAndView result = newsController.service(action, request);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainUser"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "user/newsDetail.jsp"),
                () -> assertEquals(result.getModelMap().get("news"), news)
        );
    }

    @Test
    public void service_WhenUserIsUser_ActionIsList() {
        String action = "list";
        String usertype = "user";

        News news = new News();
        List<News> newsList = new LinkedList<News>();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(newsDao.listNews(20)).thenReturn(newsList);

        ModelAndView result = newsController.service(action, request);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainUser"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "user/news.jsp"),
                () -> assertEquals(result.getModelMap().get("newsList"), newsList)
        );
    }


    @Test
    public void service_WhenUserIsUser_ActionIsOther() {
        String action = "other";
        String usertype = "user";

        News news = new News();
        List<News> newsList = new LinkedList<News>();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(newsDao.listNews(20)).thenReturn(newsList);

        ModelAndView result = newsController.service(action, request);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainUser"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "user/news.jsp"),
                () -> assertEquals(result.getModelMap().get("newsList"), newsList)
        );
    }


}