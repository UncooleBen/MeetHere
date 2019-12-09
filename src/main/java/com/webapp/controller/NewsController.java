package com.webapp.controller;

import com.webapp.model.News;
import com.webapp.service.database.dao.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Juntao Peng
 * @author Shangzhen Li
 */
@Controller
public class NewsController {

  private NewsDao newsDao;

  @Autowired
  public NewsController(NewsDao newsDao) {
    this.newsDao = newsDao;
  }

  @RequestMapping("/news")
  public ModelAndView service(
      @RequestParam("action") String action,
      HttpServletRequest request,
      HttpSession session,
      @RequestParam(value = "newsId", required = false) int newsId) {
    ModelAndView mv = new ModelAndView();
    String currentUserType = (String) session.getAttribute("currentUserType");
    if ("admin".equals(currentUserType)) {
      mv.setViewName("mainAdmin");
      adminNewsService(mv, action, newsId, request);
    } else if ("user".equals(currentUserType)) {
      mv.setViewName("mainUser");
      userNewsService(mv, action);
    }
    return mv;
  }

  private void adminNewsService(
      ModelAndView mv, String action, int newsId, HttpServletRequest request) {
    switch (action) {
      case "list":
        // TODO setViewName(mainXXXX) addObject("mainPage", "user/XXXXXX.jsp");
        // Add action "detail" addObject("news", news);
        break;
      case "add":
        mv.setViewName("admin/newsSave");
        break;
      case "delete":
        newsDao.deleteNewsById(newsId);
        mv.setViewName("admin/news");
        break;
      case "modify": {
        News news = newsDao.queryNewsById(newsId);
        mv.addObject("newsId", news.getId());
        mv.addObject("title", news.getTitle());
        mv.addObject("author", news.getAuthor());
        mv.addObject("detail", news.getDetail());
        mv.setViewName("admin/newsSave");
        break;
      }
      case "save": {
        News news = new News();
        news.setId(Integer.parseInt(request.getParameter("newsId")));
        news.setTitle(request.getParameter("title"));
        news.setAuthor(request.getParameter("author"));
        news.setDetail(request.getParameter("detail"));
        newsDao.updateNews(news);
        break;
      }
      default:
        mv.setViewName("admin/news");
        listNews(mv, "admin");
        break;
    }
  }

  private void userNewsService(ModelAndView mv, String action) {
    // TODO: need check
    mv.setViewName("user/news");
    listNews(mv, "user");
  }

  private void listNews(ModelAndView mv, String userType) {
    if ("user".equals(userType)) {
      mv.addObject("mainPage", "user/news.jsp");
    } else if ("admin".equals(userType)) {
      mv.addObject("mainPage", "admin/news.jsp");
    }
    List<News> newsList = this.newsDao.listNews(20);
    if (newsList != null) {
      mv.addObject("newsList", newsList);
    }
  }
}
