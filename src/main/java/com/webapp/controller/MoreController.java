package com.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.webapp.model.Message;
import com.webapp.service.dao.MessageDAO;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the more controller. When user wants more messages, methods in this
 * class will be called to perform query for more messages.
 * 
 * @author Juntao Peng
 */
@Controller
public class MoreController {

	@Autowired
	MessageDAO messageDAO;

	/**
	 * This method query three more messages from database according to the
	 * lastRefreshTime
	 * 
	 * @param numberOfMessage The number of message currently displays in the view
	 * @param lastRefreshTime The last refresh time as this method query three more
	 *                        message whose time stamp <= last refresh time.
	 * 
	 * @return A string object represents the messages in JSON
	 */
	@RequestMapping("/more")
	@ResponseBody
	public String more(String numberOfMessage, String lastRefreshTime) {
		List<Message> messages = this.messageDAO.queryMessage(Integer.parseInt(numberOfMessage) + 3,
				Long.parseLong(lastRefreshTime));
		Gson gson = new Gson();
		String json = gson.toJson(messages);
		return json;
	}
}
