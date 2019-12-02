package com.webapp.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.impl.LoginDaoImpl;
import com.webapp.model.user.User;

@Controller
public class SignupController {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	@Value("${spring.datasource.driver-class-name}")
	private String dbClassname;

	private DatabaseService databaseService;

	private LoginDaoImpl loginDao = new LoginDaoImpl();

	@RequestMapping("/signup")
	public ModelAndView signupPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("signup");
		return mv;
	}

	@RequestMapping("/signupSubmit")
	public ModelAndView signupSubmit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		ModelAndView mv = new ModelAndView();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String tel = request.getParameter("tel");
		String sex = request.getParameter("sex");
		Connection connection = null;
		try {
			databaseService = new DatabaseService(dbUrl, dbClassname, dbUsername, dbPassword);
			connection = databaseService.getConnection();
			User currentUser = null;
			User user = new User(username, password, name, sex, tel);
			currentUser = loginDao.Signup(connection, user);
			if (currentUser == null) {
				mv.setViewName("signup");
				mv.addObject("error", "该用户名已存在");
			} else {
				mv.setViewName("index");
				mv.addObject("user", currentUser);
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			try {
				databaseService.closeConnection(connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mv;
	}

}
