package com.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.webapp.service.database.dao.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webapp.model.News;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juntao Peng
 */
@Controller
public class NewsController {

	@Autowired
	private NewsDao newsDao;

	@RequestMapping("/news")
	public ModelAndView service(@RequestParam("action") String action, HttpServletRequest request,
								HttpSession session) {
		ModelAndView mv = new ModelAndView();
		String currentUserType = (String) session.getAttribute("currentUserType");
		if ("admin".equals(currentUserType)) {
			mv.setViewName("mainAdmin");
			adminNewsService(mv, action);
		} else if ("user".equals(currentUserType)) {
			mv.setViewName("mainUser");
			userNewsService(mv, action);
		}
		return mv;

	}

	private void adminNewsService(ModelAndView mv, String action) {
		switch (action) {
			case "list":
				//TODO 3. action = (admin-only) add (empty newsSave.jsp), delete, modify (redirect to newsSave.jsp), save (only in newsSave.jsp),
				// list (both)
			default:
				listNews(mv);
				break;
		}
	}

	private void userNewsService(ModelAndView mv, String action) {
		assert action.equals("list");
		//TODO: Finish user news service
	}

	private void listNews(ModelAndView mv) {
		mv.addObject("mainPage", "admin/news.jsp");
		List<News> newsList = this.newsDao.listNews(20);
		if (newsList != null) {
			mv.addObject("newsList", newsList);
		}
	}


}