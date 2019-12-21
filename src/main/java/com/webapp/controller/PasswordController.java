package com.webapp.controller;

import com.webapp.model.user.User;
import com.webapp.service.database.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Shangzhen Li
 */
@Controller
public class PasswordController {

    private UserDao userDao;

    @Autowired
    public PasswordController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping("/password")
    public ModelAndView service(
            @RequestParam(value = "action") String action, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession();
        String userType = (String) session.getAttribute("currentUserType");
        if ("admin".equals(userType)) {
            mv.setViewName("mainAdmin");
        } else if ("user".equals(userType)) {
            mv.setViewName("mainUser");
        }
        if ("change".equals(action)) {
            mv.addObject("mainPage", "passwordChange.jsp");
        } else if ("save".equals(action)) {
            System.out.println(request.getParameter("userId"));
            System.out.println(request.getParameter("newPassword"));
            userDao.updateUserPassword(
                    ((User) session.getAttribute("currentUser")).getId(),
                    request.getParameter("newPassword"));
        }
        return mv;
    }
}
