package com.webapp.controller;

import com.webapp.filter.LoginFilter;
import com.webapp.model.Comment;
import com.webapp.service.database.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    //TODO 2. action =  delete (admin-only), verify (admin-only)
    // list (both)
	// add

    CommentDao commentDao;

    @Autowired
    public CommentController(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @RequestMapping("/comment")
    public ModelAndView service(@RequestParam("action") String action, HttpServletRequest request,
                                HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String currentUserType = (String) session.getAttribute("currentUserType");
        boolean isAuthorized = LoginFilter.isAuthorized(currentUserType, "user", mv); /* Filter not login*/
        if (!isAuthorized) {
            return mv;
        }
		if (!"list".equals(action) && !"add".equals(action)) {
			isAuthorized = LoginFilter.isAuthorized(currentUserType, "admin", mv); /* Filter not login*/
			if (!isAuthorized) {
				return mv;
			}
		}
        String idStr = request.getParameter("id");
        int id = 0;
        if (idStr != null && idStr.length() != 0) {
            id = Integer.parseInt(idStr);
        }
        mv.setViewName("mainAdmin");
        switch (action) {
            case "delete":
                deleteComment(mv, id);
                listComments(mv, currentUserType);
                if ("admin".equals(currentUserType)) {
                	listUnverifiedComments(mv);
				}
                break;
            case "add":
                addComment(mv);
                break;
            case "save":
                saveComment(mv, request, session);
                listComments(mv, currentUserType);
                break;
            case "list":
            default:
                listComments(mv, currentUserType);
                if ("admin".equals(currentUserType)) {
                	listUnverifiedComments(mv);
				}
        }
        return mv;
    }

    public void listComments(ModelAndView mv, String currentUserType) {
        List<Comment> commentList = this.commentDao.listComment(20, true);
        if ("admin".equals(currentUserType)) {
            mv.setViewName("mainAdmin");
            mv.addObject("mainPage", "admin/comment.jsp");
        } else {
            mv.setViewName("mainUser");
            mv.addObject("mainPage", "user/comment.jsp");
        }
        mv.addObject("commentList", commentList);
    }

	public void listUnverifiedComments(ModelAndView mv) {
		List<Comment> unverifiedCommentList = this.commentDao.listComment(20, false);
		mv.addObject("unverifiedCommentList", unverifiedCommentList);
	}

    public void deleteComment(ModelAndView mv, int id) {
        this.commentDao.deleteComment(id);
    }

    public void addComment(ModelAndView mv) {
        mv.addObject("mainPage", "admin/commentModify.jsp");
        mv.addObject("id", null);
        mv.addObject("comment", null);
    }

    public void saveComment(ModelAndView mv, HttpServletRequest request, HttpSession session) {
        String userIdStr = (String) session.getAttribute("userId");
        int userId = Integer.parseInt(userIdStr);
        long date = System.currentTimeMillis();
        String content = request.getParameter("content");
        Comment comment = new Comment(userId, date, content);
        this.commentDao.addComment(comment);
    }
}