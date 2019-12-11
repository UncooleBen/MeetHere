package com.webapp.controller;

import com.webapp.model.user.Admin;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Juntao Peng
 */
@Controller
public class LoginController {


	LoginDao loginDao;

	@Autowired
	public LoginController(LoginDao loginDao) {
		this.loginDao = loginDao;
	}

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
				session.setAttribute("currentUserType", "user");
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
