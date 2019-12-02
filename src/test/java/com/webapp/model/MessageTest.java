package com.webapp.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is a Test for Message.class.
 * 
 * @author Yuanjie Guo
 */
class MessageTest {

	@Test
	void test_newMessage_by_username_content_time() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_string = "2019-11-01 12:50:20";
		String username = "彭钧涛";
		String content = "彭哥牛逼";
		String ago = "Error";
		String path = null;
		Date date = null;

		try {
			date = format.parse(date_string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = new Message(username, content, date);
		assertAll(() -> assertEquals(username, message.get_username()),
				() -> assertEquals(content, message.get_content()),
				() -> assertEquals(date_string, format.format(message.get_time())),
				() -> assertNotNull(message.get_uuid()), () -> assertNotNull(message.get_uuidstr()),
				() -> assertEquals(ago, message.get_ago()), () -> assertEquals(path, message.get_path()));

	}

	@Test
	void test_newMessage_by_uuid_username_content_time_path() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_string = "2019-11-01 12:50:20";
		String username = "彭俊涛";
		String content = "彭哥牛逼";
		String path = "my computer";
		UUID uuid = UUID.randomUUID();
		Date date = null;
		String uuidstr = uuid.toString();
		try {
			date = format.parse(date_string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = new Message(uuid, username, content, date, path);
		assertAll(() -> assertEquals(username, message.get_username()),
				() -> assertEquals(content, message.get_content()),
				() -> assertEquals(date_string, format.format(message.get_time())),
				() -> assertEquals(path, message.get_path()), () -> assertEquals(uuidstr, message.get_uuidstr()),
				() -> assertEquals(uuid, message.get_uuid()));

	}

	@Test
	void test_newMessage_by_uuid_username_content_time() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_string = "2019-11-01 12:50:20";
		String username = "彭俊涛";
		String content = "彭哥牛逼";
		String path = null;
		UUID uuid = UUID.randomUUID();
		Date date = null;
		String uuidstr = uuid.toString();
		try {
			date = format.parse(date_string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = new Message(uuid, username, content, date);
		assertAll(() -> assertEquals(username, message.get_username()),
				() -> assertEquals(content, message.get_content()),
				() -> assertEquals(date_string, format.format(message.get_time())),
				() -> assertEquals(path, message.get_path()), () -> assertEquals(uuidstr, message.get_uuidstr()),
				() -> assertEquals(uuid, message.get_uuid()));

	}


	static Stream<Arguments> agoProvider() {
		return Stream.of(Arguments.of(new Message("彭俊涛", "彭哥牛逼", new Date(1000)), 1500, "Just Now"),
				Arguments.of(new Message("彭俊涛", "彭哥牛逼", new Date(1000)), 3000, 2 + " Second(s) Ago"),
				Arguments.of(new Message("彭俊涛", "彭哥牛逼", new Date(1000)), 61000, 1 + " Minute(s) Ago"),
				Arguments.of(new Message("彭俊涛", "彭哥牛逼", new Date(1000)), 3601000, 1 + " Hours(s) Ago"),
				Arguments.of(new Message("彭俊涛", "彭哥牛逼", new Date(1000)), 360000000, "1970-01-01 08:00:01"));
	}

	@ParameterizedTest
	@MethodSource("agoProvider")
	void test_set_ago(Message message, long millisec, String result) {
		message.set_ago(millisec);
		assertEquals(result, message.get_ago());
	}



	@Test
	void test_toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_string = "2019-11-01 12:50:20";
		String username = "彭俊涛";
		String content = "彭哥牛逼";

		Date date = null;
		try {
			date = format.parse(date_string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = new Message(username, content, date);
		String result = "Message { username : " + username + " content : " + content + " date : " + date + " }";
		assertEquals(result, message.toString());
	}

	static Stream<Arguments> MessageProvider() {
		return Stream.of(Arguments.of(new Message("彭俊涛", "彭哥牛逼", new Date(1000)), null, false), Arguments
				.of(new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭俊涛", "彭哥牛逼", new Date(1000)),
						new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1d"), "彭俊涛", "彭哥牛逼",
								new Date(1000)),
						false),
				Arguments.of(
						new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭俊涛", "彭哥牛逼",
								new Date(1000)),
						new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭俊涛", "彭哥牛逼",
								new Date(2001001)),
						false),
				Arguments.of(
						new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭俊涛", "彭哥牛逼",
								new Date(1000)),
						new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭钧涛", "彭哥牛逼",
								new Date(1000)),
						false),
				Arguments.of(
						new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭俊涛", "彭哥牛逼",
								new Date(1000)),
						new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭俊涛", "彭钧牛逼",
								new Date(1000)),
						false),
				Arguments.of(
						new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭俊涛", "彭哥牛逼",
								new Date(1000)),
						new Message(UUID.fromString("0c312388-5d09-4f44-b670-5461605f0b1e"), "彭俊涛", "彭哥牛逼",
								new Date(1000)),
						true));
	}

	@ParameterizedTest
	@MethodSource("MessageProvider")
	void test_equals(Message my_message, Message other_message, boolean result) {
		assertEquals(result, my_message.equals(other_message));
	}
}
