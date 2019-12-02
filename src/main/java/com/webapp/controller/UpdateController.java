package com.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.service.dao.MessageDAO;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the update controller. Methods of the class handles the AJAX requests
 * that related to the number in the UPDATE button, which indicates number of
 * new messages.
 * 
 * @author Juntao Peng
 */
@Controller
public class UpdateController {

	@Autowired
	MessageDAO messageDAO;

	/**
	 * This method query how many new messages are there in the database since the
	 * last refresh time.
	 * 
	 * @param lastRefreshTime A string passed from view (web page) of last refresh
	 *                        time
	 * 
	 * @return A string in the form of "X Update(s)"
	 *
	 * @author Juntao Peng
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String update(String lastRefreshTime) {
		String result = " Update(s)";
		long lastRefreshTimeLong = Long.parseLong(lastRefreshTime);
		int new_messages = this.messageDAO.queryUpdates(lastRefreshTimeLong);
		result = new_messages + result;
		return result;
	}

}
