package com.webapp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.webapp.service.dao.MessageDAO;

/**
 * This class is a part of Software-Testing lab02 timeline.
 *
 * <p>
 * This is a Test for IndexController.class.
 *
 * @author Yuanjie Guo
 */

class IndexControllerTest {

	private MessageDAO messageDAO = mock(MessageDAO.class);
	private IndexController indexController = new IndexController();

	@Test
	void test_display() {
		indexController.messageDAO = messageDAO;
		System system;
		ModelAndView result = indexController.display();
		String currentTime = String.valueOf(System.currentTimeMillis());
		Object time = result.getModel().get("lastRefreshTime");
		Object number = result.getModel().get("numberOfMessage");
		String view = result.getViewName();

		assertAll(() -> assertEquals(view, "index"), () -> assertEquals((number), String.valueOf(0)));

	}

}
