package com.webapp.filter;

import org.springframework.web.servlet.ModelAndView;

public class LoginFilter {

    public static boolean isAuthorized(String currentUserType, String authorizedUserType, ModelAndView mv) {
        if (currentUserType == null || "".equals(currentUserType)) { /* Not login */
            mv.setViewName("index");
            return false;
        }
        if ("user".equals(authorizedUserType)) { /* Login and authorized user type */
            return true;
        }
        if ("admin".equals(authorizedUserType) && "admin".equals(currentUserType)) { /* Login and authorized user type */
            return true;
        }
        /* Login and unauthorized user type */
        mv.setViewName("mainUser");
        mv.addObject("mainPage", "blank.jsp");
        return false;
    }

}
