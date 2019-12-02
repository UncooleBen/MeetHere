package com.webapp.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.webapp.model.Message;
import com.webapp.service.dao.MessageDAO;
import com.webapp.service.filesystem.Fao;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the new message controller. When user comment a new message, methods
 * in this class will be called to perform update and refresh for the view.
 * 
 * @author Juntao Peng
 */
@Controller
public class NewMessageController {

	@Autowired
	MessageDAO messageDAO;
	@Autowired
    Fao fao;

	/**
	 * This method is called when user hits the submit button in web page
	 * 
	 * @param username A string of username
	 * @param content  A string of message content
	 * 
	 * @return A ModelAndView object of refresh of the current web page
	 */
	@RequestMapping(value = "/newMessage", method = RequestMethod.POST)
	public ModelAndView onSubmit(@RequestParam("username") String username, @RequestParam("content") String content,
			@RequestParam(value = "image", required = false) MultipartFile image) {
		Message newMessage = new Message(username, content, new Date(System.currentTimeMillis()));
		if (image.isEmpty()) {
			this.messageDAO.storeMessage(newMessage, false);
		} else {
			this.messageDAO.storeMessage(newMessage, true);
			this.fao.storeImage(newMessage, image);
		}
		ModelAndView mv = new ModelAndView("redirect:/");
		return mv;
	}

}
