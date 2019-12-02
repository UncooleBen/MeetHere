package com.webapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.webapp.model.Message;
import com.webapp.service.dao.MessageDAO;

/**
 * This class is a part of Software-Testing lab02 timeline.
 *
 * <p>
 * This is a Test for  MoreController.class.
 *
 * @author Yuanjie Guo
 */

class MoreControllerTest {

	private MessageDAO messageDAO = mock(MessageDAO.class);
	private MoreController moreController = new MoreController();

	@Test
	void test_more() {
		moreController.messageDAO = messageDAO;
		List<Message> messages = new ArrayList<Message>();
		messages.add(
				new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭钧涛", "彭哥牛逼", new Date(1000)));
		when(messageDAO.queryMessage(4, 2)).thenReturn(messages);

		String result = "[{\"_username\":\"彭钧涛\",\"_content\":\"彭哥牛逼\",\"_time\":\"Jan 1, 1970, 8:00:01 AM\",\"_uuid\":\"0c312388-5d09-4f44-b670-5461605f0b1e\",\"_uuidstr\":\"0c312388-5d09-4f44-b670-5461605f0b1e\"}]";
		assertEquals(moreController.more("1", "2"), result);
	}

}
