package com.webapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.webapp.service.dao.MessageDAO;

/**
 * This class is a part of Software-Testing lab02 timeline.
 *
 * <p>
 * This is a Test for UpdateController.class.
 *
 * @author Yuanjie Guo
 */

class UpdateControllerTest {

	private MessageDAO messageDAO = mock(MessageDAO.class);
	private UpdateController updateController = new UpdateController();

	@Test
	void test_update() {
		updateController.messageDAO = messageDAO;
		when(messageDAO.queryUpdates(1)).thenReturn(2);

		String result = 2 + " Update(s)";
		assertEquals(updateController.update("1"), result);
	}

}
