package com.webapp.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.webapp.service.database.dao.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.impl.LoginDaoImpl;
import com.webapp.model.user.Admin;
import com.webapp.model.user.User;

@Controller
public class LoginController {

	@Autowired
	private LoginDao loginDao;

	@RequestMapping("/login")
	public ModelAndView service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ModelAndView mv = new ModelAndView();
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		String login_username = request.getParameter("username");
		String login_password = request.getParameter("password");
		User result_user = loginDao.login(login_username, login_password);
		if (result_user != null) {
			if (result_user instanceof Admin) {
				Admin admin = (Admin) result_user;
				mv.setViewName("mainAdmin");
				session.setAttribute("currentUserType", "admin");
				session.setAttribute("currentUser", admin);
				mv.addObject("admin", admin);
			} else if (result_user instanceof User) {
				User user = result_user;
				mv.setViewName("mainUser");
				session.setAttribute("currentUserType", "admin");
				session.setAttribute("currentUser", user);
				mv.addObject("user", user);
			}
		} else if (result_user == null) {
			mv.setViewName("index");
			mv.addObject("error", "用户名或密码错误");
		}
		return mv;
	}

}
