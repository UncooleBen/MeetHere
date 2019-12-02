package com.webapp.service.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;

import com.webapp.model.Message;
import org.mockito.InOrder;


/**
 * This class is the unit test of class MessageDaoMysqlImpl.
 *
 * @author Juntao Peng
 * @date 2019.11.27
 */
@TestMethodOrder(OrderAnnotation.class)
class MessageDaoMysqlImplTest {

    private Connection connection = mock(Connection.class);
    private PreparedStatement preparedStatement = mock(PreparedStatement.class);
	private ResultSet rs = mock(ResultSet.class);
    private MessageDaoMysqlImpl messageDAO;
    private long milliTime;
    private SimpleDateFormat format;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private SQLException test_sql_exception;
    private ArgumentCaptor<String> stringArgumentCaptor;
	private ArgumentCaptor<Boolean> booleanArgumentCaptor;
	private ArgumentCaptor<Integer> integerArgumentCaptor;

    class TestableMessageDaoMysqlImpl extends MessageDaoMysqlImpl {

        @Override
        protected void loadDriver() {

        }

        @Override
        protected Connection getConnection() {
            return connection;
        }
    }

    @BeforeEach
    void init() {
        this.messageDAO = new TestableMessageDaoMysqlImpl();
        this.milliTime = System.currentTimeMillis();
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.test_sql_exception = new SQLException();
        this.outContent = new ByteArrayOutputStream();
        this.errContent = new ByteArrayOutputStream();
        this.originalOut = System.out;
        this.originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
		this.stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		this.booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
		this.integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
    }

    @AfterEach
    void tear_down() throws IOException {
        System.setErr(this.originalErr);
        System.setOut(this.originalOut);
        this.outContent.close();
        this.errContent.close();
    }

    @Order(1)
    @Test
    void test_throws_sql_exception_when_store_message() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sql_exception);
        this.messageDAO.storeMessage(mock(Message.class), false);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Order(2)
    @Test
    void test_store_one_message_without_image() throws SQLException {
        Date date = new Date(milliTime);
        Message message = new Message("james", "im bond", date);
        /* Mocking behaviours */
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        /* Run the method */
        boolean succeeded = this.messageDAO.storeMessage(message, false);
        /* Assertions */
        assertTrue(succeeded);
        InOrder order = inOrder(preparedStatement);
        order.verify(preparedStatement, times(4)).setString(anyInt(), stringArgumentCaptor.capture());
        order.verify(preparedStatement, times(1)).setBoolean(anyInt(), booleanArgumentCaptor.capture());
        order.verify(preparedStatement, times(1)).setString(anyInt(), stringArgumentCaptor.capture());
        assertAll(
				() -> assertEquals("james", stringArgumentCaptor.getAllValues().get(1)),
				() -> assertEquals("im bond", stringArgumentCaptor.getAllValues().get(2)),
				() -> assertNull(stringArgumentCaptor.getAllValues().get(4)),
				() -> assertFalse(booleanArgumentCaptor.getAllValues().get(0))
		);
    }

    @Order(3)
    @Test
    void test_store_one_message_with_image() throws SQLException {
        Date date = new Date(milliTime);
        Message message = new Message("james", "im bond", date);
        /* Mocking behaviours */
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        /* Run the method */
        boolean succeeded = this.messageDAO.storeMessage(message, true);
        /* Assertions */
		InOrder order = inOrder(preparedStatement);
		order.verify(preparedStatement, times(4)).setString(anyInt(), stringArgumentCaptor.capture());
		order.verify(preparedStatement, times(1)).setBoolean(anyInt(), booleanArgumentCaptor.capture());
        order.verify(preparedStatement, times(1)).setString(anyInt(), stringArgumentCaptor.capture());
        assertAll(
				() -> assertTrue(succeeded),
				() -> assertEquals("james", stringArgumentCaptor.getAllValues().get(1)),
				() -> assertEquals("im bond", stringArgumentCaptor.getAllValues().get(2)),
				() -> assertEquals(System.getenv("TEMP") + "\\timeline_imgs", stringArgumentCaptor.getAllValues().get(4)),
				() -> assertEquals(true, booleanArgumentCaptor.getAllValues().get(0))
		);
    }

    @Order(4)
    @Test
    void test_throws_sql_exception_when_query_by_uuid() throws SQLException {
    	UUID uuid = UUID.randomUUID();
		/* Mocking behaviours */
    	when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sql_exception);
		/* Run the method */
        this.messageDAO.queryMessageByUUID(uuid);
		/* Assertions */
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Order(5)
    @Test
    void test_throws_parse_exception_when_query_by_uuid() throws SQLException {
        UUID uuid = UUID.randomUUID();
        String uuid_str = uuid.toString();
        String username = "james bond";
        String content = "hello";
        String time = "MALFORMED DATE STRING";
        /* Mocking behaviors */
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true, false); // First call returns true, second call returns false
		when(rs.getString("uuid")).thenReturn(uuid_str);
		when(rs.getString("username")).thenReturn(username);
		when(rs.getString("content")).thenReturn(content);
		when(rs.getString("time")).thenReturn(time);
        /* Run the method */
		this.messageDAO.queryMessageByUUID(uuid);
        /* Assertions */
		assertTrue(errContent.toString().contains("java.text.ParseException"));
    }

    @Order(6)
    @Test
    void test_query_message_by_uuid() throws SQLException {
        UUID uuid = UUID.randomUUID();
        String uuid_str = uuid.toString();
        String username = "james bond";
        String content = "hello";
        String time = this.format.format(new Date(this.milliTime));
        /* Mocking behaviors */
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
		when(rs.getString("uuid")).thenReturn(uuid_str);
		when(rs.getString("username")).thenReturn(username);
		when(rs.getString("content")).thenReturn(content);
		when(rs.getString("time")).thenReturn(time);
        /* Run the method */
		List<Message> result_list = this.messageDAO.queryMessageByUUID(uuid);
        /* Assertions */
		InOrder order = inOrder(preparedStatement);
		order.verify(preparedStatement, times(1)).setString(anyInt(), stringArgumentCaptor.capture());
        Message result_message = result_list.get(0);
        assertAll(
				() -> assertEquals(1, result_list.size()),
                () -> assertEquals(uuid, result_message.get_uuid()),
                () -> assertEquals(username, result_message.get_username()),
                () -> assertEquals(content, result_message.get_content()),
                () -> assertEquals(time, format.format(result_message.get_time())),
				() -> assertEquals(uuid_str, stringArgumentCaptor.getAllValues().get(0))
        );
    }

    @Order(7)
    @Test
    void test_throws_sql_exception_when_query_message() throws SQLException {
    	/* Mocking behaviors */
    	when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sql_exception);
        /* Run the method */
    	this.messageDAO.queryMessage(0, 0L);
    	/* Assertions */
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Order(8)
    @Test
    void test_throws_parse_exception_when_query_message() throws SQLException {
        UUID uuid = UUID.randomUUID();
        String uuid_str = uuid.toString();
        String username = "james bond";
        String content = "hello";
        String time = "MALFORMED DATE STRING";
        /* Mocking behaviors */
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true, false); // First call returns true, second call returns false
		when(rs.getString("uuid")).thenReturn(uuid_str);
		when(rs.getString("username")).thenReturn(username);
		when(rs.getString("content")).thenReturn(content);
		when(rs.getString("time")).thenReturn(time);
        /* Run the method */
        this.messageDAO.queryMessage(0, 0L);
        /* Assertions */
        assertTrue(errContent.toString().contains("java.text.ParseException"));
    }

    @Order(9)
    @Test
    void test_query_message() throws SQLException {
    	/* Prepare suppliers */
        List<Message> expected = new ArrayList<Message>(5);
        for (int index = 0; index < 5; index++) {
            UUID uuid = UUID.randomUUID();
            String username = "USER" + UUID.randomUUID().toString();
            String content = "CONTENT" + UUID.randomUUID().toString();
            Date time = new Date(this.milliTime);
            Message newMessage = new Message(uuid, username, content, time);
            expected.add(newMessage);
        }
        Supplier<Stream<String>> uuids = () -> expected.stream().map(Message::get_uuid).map(UUID::toString);
        Supplier<Stream<String>> usernames = () -> expected.stream().map(Message::get_username);
        Supplier<Stream<String>> contents = () -> expected.stream().map(Message::get_content);
        Supplier<Stream<String>> times = () -> expected.stream().map(Message::get_time)
                .map(date -> (this.format.format(date)));
		/* Mocking behaviors */
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true, true, true, true, true, false); /* First call returns
		 true, sequential calls return true, true, true, true, false. */
		when(rs.getString("uuid")).thenReturn(uuids.get().findFirst().get(),
				uuids.get().skip(1).toArray(String[]::new));
		when(rs.getString("username")).thenReturn(usernames.get().findFirst().get(),
				usernames.get().skip(1).toArray(String[]::new));
		when(rs.getString("content")).thenReturn(contents.get().findFirst().get(),
				contents.get().skip(1).toArray(String[]::new));
		when(rs.getString("time")).thenReturn(times.get().findFirst().get(),
				times.get().skip(1).toArray(String[]::new));
        /* Run the method */
        List<Message> actual = this.messageDAO.queryMessage(5, milliTime);
        /* Assertions */
		InOrder order = inOrder(preparedStatement);
		order.verify(preparedStatement, times(1)).setString(eq(1), stringArgumentCaptor.capture());
		order.verify(preparedStatement, times(1)).setInt(eq(2), integerArgumentCaptor.capture());
		assertAll(
					() -> assertEquals(5, actual.size()),
					() -> assertEquals(expected.get(0), actual.get(0)),
					() -> assertEquals(expected.get(1), actual.get(1)),
					() -> assertEquals(expected.get(2), actual.get(2)),
					() -> assertEquals(expected.get(3), actual.get(3)),
					() -> assertEquals(expected.get(4), actual.get(4)),
					() -> assertEquals(this.format.format(new Date(milliTime)), stringArgumentCaptor.getAllValues().get(0)),
					() -> assertEquals(5, integerArgumentCaptor.getAllValues().get(0))
		);
    }

    @Order(10)
    @Test
    void test_throws_sql_exception_when_query_updates() throws SQLException {
    	/* Mocking behaviors */
    	when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sql_exception);
    	/* Run the method */
        this.messageDAO.queryUpdates(0L);
        /* Assertions */
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Order(11)
    @Test
    void test_query_updates() throws SQLException {
        int expected = 5;
        /* Mocking behaviors */
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true, false);
		when(rs.getInt(1)).thenReturn(expected);
		/* Run the method */
        int actual = this.messageDAO.queryUpdates(milliTime);
        /* Assertions */
		InOrder order = inOrder(preparedStatement);
		order.verify(preparedStatement, times(1)).setString(eq(1), stringArgumentCaptor.capture());
        assertAll(
					() -> assertEquals(expected, actual),
					() -> assertEquals(this.format.format(new Date(milliTime)), stringArgumentCaptor.getAllValues().get(0))
		);
    }

    @Order(12)
    @Test
    void test_sql_exception_clear_table() throws SQLException {
    	/* Mocking behaviors */
        when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sql_exception);
        /* Run the method */
        this.messageDAO.clearTable();
        /* Assertions */
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Order(13)
    @Test
    void test_clear_table() throws SQLException {
		/* Mocking behaviors */
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
		/* Run the method */
        boolean success = this.messageDAO.clearTable();
		/* Assertions */
        assertTrue(success);
    }

    @Order(14)
    @Test
    void test_sql_exception_close_statement() throws SQLException {
		/* Mocking behaviors */
        doThrow(test_sql_exception).when(preparedStatement).close();
		/* Run the method */
        this.messageDAO.closeStatementAndConnection(preparedStatement, connection);
		/* Assertions */
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Order(15)
    @Test
    void test_sql_exception_close_connection() throws SQLException {
		/* Mocking behaviors */
        doThrow(test_sql_exception).when(connection).close();
		/* Run the method */
        this.messageDAO.closeStatementAndConnection(preparedStatement, connection);
		/* Assertions */
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

}
