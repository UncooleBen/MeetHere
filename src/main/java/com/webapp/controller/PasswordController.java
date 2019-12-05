package com.webapp.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webapp.service.database.dao.impl.LoginDaoImpl;
import com.webapp.model.Admin;
import com.webapp.model.User;
import com.webapp.util.DbUtil;

@Controller
public class PasswordController {
	//TODO 4. action = save (both) change (redirect to passwordChange.jsp, both)

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	@Value("${spring.datasource.driver-class-name}")
	private String dbClassname;

	private DbUtil dbUtil;

	private LoginDaoImpl loginDao = new LoginDaoImpl();

	@RequestMapping("/password")
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");

		if ("preChange".equals(action)) {
			passwordPreChange(request, response);
			return;
		} else if ("change".equals(action)) {
			passwordChange(request, response);
			return;
		}
	}

	private void passwordChange(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		Connection con = null;
		try {
			dbUtil = new DbUtil(dbUrl, dbClassname, dbUsername, dbPassword);
			con = dbUtil.getCon();

			if ("admin".equals(currentUserType)) {
				Admin admin = (Admin) (session.getAttribute("currentUser"));
				if (oldPassword.equals(admin.getPassword())) {
					loginDao.adminUpdate(con, admin.getAdminId(), newPassword);
					admin.setPassword(newPassword);
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "�޸ĳɹ� ");
					request.setAttribute("mainPage", "admin/passwordChange.jsp");
					request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				} else {
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "ԭ�������");
					request.setAttribute("mainPage", "admin/passwordChange.jsp");
					request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				}
			} else if ("user".equals(currentUserType)) {
				User user = (User) (session.getAttribute("currentUser"));
				if (oldPassword.equals(user.getPassword())) {
					loginDao.adminUpdate(con, user.getUserId(), newPassword);
					user.setPassword(newPassword);
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "�޸ĳɹ� ");
					request.setAttribute("mainPage", "user/passwordChange.jsp");
					request.getRequestDispatcher("mainUser.jsp").forward(request, response);
				} else {
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "ԭ�������");
					request.setAttribute("mainPage", "user/passwordChange.jsp");
					request.getRequestDispatcher("mainUser.jsp").forward(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void passwordPreChange(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		if ("admin".equals(currentUserType)) {
			request.setAttribute("mainPage", "admin/passwordChange.jsp");
			request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
		} else if ("user".equals(currentUserType)) {
			request.setAttribute("mainPage", "user/passwordChange.jsp");
			request.getRequestDispatcher("mainUser.jsp").forward(request, response);
		}
	}

}
