package com.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.webapp.service.database.dao.impl.UserDaoImpl;
import com.webapp.model.user.Gender;
import com.webapp.model.user.User;

/**
 * @author Juntao Peng
 */
@Controller
public class UserController {

    @Autowired
    private UserDaoImpl userDao;

    @RequestMapping(value = "/user")
    public ModelAndView service(@RequestParam("action") String action, HttpServletRequest request,
                                HttpSession session) {
        String currentUserType = (String) session.getAttribute("currentUserType");
        assert currentUserType == "admin";
        String id_str = request.getParameter("id");
        int id = 0;
        if (id_str != null && id_str.length() != 0) {
            id = Integer.parseInt(id_str);
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("mainAdmin");
        switch (action) {
            case "delete":
                delete_user(mv, id);
                list_users(mv);
                break;
            case "modify":
                modify_user(mv, id);
                break;
            case "add":
                add_user(mv);
                break;
            case "save":
                save_user(mv, request);
                list_users(mv);
                break;
            case "search":
                search_user(mv, request);
                break;
            case "list":
            default:
                list_users(mv);
        }
        return mv;
    }

    private void list_users(ModelAndView mv) {
        mv.addObject("mainPage", "admin/user.jsp");
        List<User> users = this.userDao.query_all_users();
        if (users.size() > 0) {
            mv.addObject("user_list", users);
        }

    }

    private void delete_user(ModelAndView mv, int id) {
        this.userDao.delete_user(id);
    }

    private void modify_user(ModelAndView mv, int id) {
        User user = this.userDao.query_user_by_id(id);
        mv.addObject("mainPage", "admin/userModify.jsp");
        mv.addObject("id", String.valueOf(id));
        mv.addObject("user", user);
    }

    private void add_user(ModelAndView mv) {
        mv.addObject("mainPage", "admin/userModify.jsp");
        mv.addObject("id", null);
        mv.addObject("user", null);
    }

    private void save_user(ModelAndView mv, HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String tel = request.getParameter("tel");
        String sex = request.getParameter("sex");
        User user = new User(username, password, name, sex, tel);
        String id_str = request.getParameter("id");

        if (id_str != null && id_str.length() != 0) {
            int id = Integer.valueOf(id_str);
            user.set_id(id);
            this.userDao.update_user_all(user);
        } else {
            this.userDao.add_user(user);
        }

    }

    private void search_user(ModelAndView mv, HttpServletRequest request) {
        String keyword = request.getParameter("searchType");
        String argument = request.getParameter("search_user_text");
        mv.addObject("mainPage", "admin/user.jsp");
        assert "name".equals(keyword) || "id".equals(keyword) || "sex".equals(keyword);
        if (argument == null || argument.length() == 0) {
            list_users(mv);
            return;
        }

        List<User> result_list = null;
        switch (keyword) {
            case "name":
                result_list = this.userDao.query_user_by_name(argument);
                break;
            case "sex":
                result_list = this.userDao.query_user_by_sex(Gender.valueOf(argument));
                break;
            case "id":
                result_list = new ArrayList<>();
                User result = this.userDao.query_user_by_id(Integer.valueOf(argument));
                if (result != null) {
                    result_list.add(result);
                }
            default:
        }
        if (result_list.size() > 0) {
            mv.addObject("user_list", result_list);
        }

    }

}
