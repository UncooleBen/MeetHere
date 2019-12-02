package com.webapp.service.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.webapp.model.Message;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

/**
 * This class is a unit test of class MessageSQLServerDAO.
 *
 * @author Shangzhen Li
 */
public class MessageSQLServerDAOTest {
  private MessageSQLServerDAO messageDAO;
  private Connection connection = mock(Connection.class);
  private PreparedStatement pstmt = mock(PreparedStatement.class);
  private Message message = mock(Message.class);
  private ResultSet rs = mock(ResultSet.class);
  private Locale defaultLocale;
  private ByteArrayOutputStream errContent;
  private PrintStream originalErr;
  private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  private final String TEST_USERNAME = "testUsername";
  private final String TEST_CONTENT = "testContent";
  private final String TEST_UUID = "0-0-0-0-0";
  private final String TEST_TIME = "2019-10-30 12:00:00";
  private final String TEST_PATH = "testPath";
  private final long TEST_MILLISEC = 1572364800000L; /* Equal to TEST_TIME */
  private final String TEST_USERNAME2 = "testUsername2";
  private final String TEST_CONTENT2 = "testContent2";
  private final String TEST_UUID2 = "0-0-0-0-1";
  private final String TEST_TIME2 = "2019-10-30 08:00:00";

  class MessageSQLServerDAOFake extends MessageSQLServerDAO {
    @Override
    protected void loadDriver() {}

    @Override
    protected Connection getConnection() {
      return connection;
    }
  }

  @BeforeEach
  void init() {
    messageDAO = new MessageSQLServerDAOFake();
    defaultLocale = Locale.getDefault();
    Locale.setDefault(Locale.CHINA);
    /* Change error output stream to capture error output */
    errContent = new ByteArrayOutputStream();
    originalErr = System.err;
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  void restore() {
    Locale.setDefault(defaultLocale);
    /* Change error output stream back to default */
    System.setErr(originalErr);
  }

  @Test
  void testStoreNullMessageWithoutImageThrowsException() {
    assertThrows(NullPointerException.class, () -> messageDAO.storeMessage(null, true));
  }

  @Test
  void testStoreOneMessageWithoutImage() throws Exception {
    String INSERT =
        "INSERT INTO message(uuid, username, content, time, withImage, path) VALUES(?,?,?,?,?,?)";
    Date date = mock(Date.class);
    /* Stub */
    when(connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    when(message.get_uuid()).thenReturn(UUID.fromString(TEST_UUID));
    when(message.get_username()).thenReturn(TEST_USERNAME);
    when(message.get_content()).thenReturn(TEST_CONTENT);
    when(message.get_time()).thenReturn(date);
    /* Test return value */
    boolean succeed = messageDAO.storeMessage(message, false);
    assertTrue(succeed);
    /* Test function calls' order and capture arguments */
    InOrder order = inOrder(pstmt, connection);
    ArgumentCaptor<String> argsCap = ArgumentCaptor.forClass(String.class);
    order.verify(connection).prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
    order.verify(pstmt, times(4)).setString(anyInt(), argsCap.capture());
    order.verify(pstmt).setBoolean(5, false);
    order.verify(pstmt).setString(6, null);
    order.verify(pstmt).execute();
    order.verify(pstmt).close();
    order.verify(connection).close();
    order.verifyNoMoreInteractions();
    /* Test arguments' order and value */
    assertEquals(TEST_USERNAME, argsCap.getAllValues().get(1));
    assertEquals(TEST_CONTENT, argsCap.getAllValues().get(2));
  }

  @Test
  void testStoreMessageWithoutImageThrowsSQLException() throws Exception {
    /* Stub */
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
    /* Test return value */
    boolean succeed = messageDAO.storeMessage(message, false);
    assertFalse(succeed);
    /* Test error output */
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
  }

  @Test
  void testStoreNullMessageWithImageThrowsException() {
    assertThrows(NullPointerException.class, () -> messageDAO.storeMessage(null, true));
  }

  @Test
  void testStoreOneMessageWithImage() throws Exception {
    String INSERT =
        "INSERT INTO message(uuid, username, content, time, withImage, path) VALUES(?,?,?,?,?,?)";
    Date date = mock(Date.class);
    messageDAO = spy(messageDAO);
    /* Stub */
    when(connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    when(message.get_uuid()).thenReturn(UUID.fromString(TEST_UUID));
    when(message.get_username()).thenReturn(TEST_USERNAME);
    when(message.get_content()).thenReturn(TEST_CONTENT);
    when(message.get_time()).thenReturn(date);
    /* Test return value */
    boolean succeed = messageDAO.storeMessage(message, true);
    assertTrue(succeed);
    /* Test function calls' order and capture arguments */
    InOrder order = inOrder(pstmt, connection, messageDAO);
    ArgumentCaptor<String> argsCap = ArgumentCaptor.forClass(String.class);
    order.verify(connection).prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
    order.verify(pstmt, times(4)).setString(anyInt(), argsCap.capture());
    order.verify(pstmt).setBoolean(5, true);
    order.verify(pstmt).setString(eq(6), anyString());
    order.verify(pstmt).execute();
    order.verify(pstmt).close();
    order.verify(connection).close();
    order.verifyNoMoreInteractions();
    /* Test arguments' order and value */
    assertEquals(TEST_USERNAME, argsCap.getAllValues().get(1));
    assertEquals(TEST_CONTENT, argsCap.getAllValues().get(2));
  }

  @Test
  void testStoreMessageWithImageThrowsSQLException() throws Exception {
    messageDAO = spy(messageDAO);
    /* Stub */
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
    when(message.get_uuid()).thenReturn(UUID.fromString(TEST_UUID));
    /* Test return value */
    boolean succeed = messageDAO.storeMessage(message, true);
    assertFalse(succeed);
    /* Test error output */
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
  }

  @Test
  void testQueryMessageByUUIDReturnNoImage() throws Exception {
    String SELECT = "SELECT * FROM message WHERE uuid=(?)";
    /* Stub */
    when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    when(pstmt.executeQuery()).thenReturn(rs);
    when(rs.getString("uuid")).thenReturn(TEST_UUID);
    when(rs.getString("username")).thenReturn(TEST_USERNAME);
    when(rs.getString("content")).thenReturn(TEST_CONTENT);
    when(rs.getString("time")).thenReturn(TEST_TIME);
    when(rs.getBoolean("withImage")).thenReturn(false);
    when(rs.next()).thenReturn(true, false); /* Only one item in ResultSet */
    /* Test return value */
    List<Message> resultList = messageDAO.queryMessageByUUID(UUID.fromString(TEST_UUID));
    assertEquals(1, resultList.size());
    assertEquals(UUID.fromString(TEST_UUID), resultList.get(0).get_uuid());
    assertEquals(TEST_USERNAME, resultList.get(0).get_username());
    assertEquals(TEST_CONTENT, resultList.get(0).get_content());
    assertEquals(TEST_TIME, dateFormatter.format(resultList.get(0).get_time()));
    assertNull(resultList.get(0).get_path());
    /* Test function calls' order */
    InOrder order = inOrder(connection, pstmt, rs);
    order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
    order.verify(pstmt).executeQuery();
    order.verify(rs).next();
    order.verify(rs, times(4)).getString(anyString());
    order.verify(rs).getBoolean(anyString());
    order.verify(pstmt).close();
    order.verify(connection).close();
    order.verifyNoMoreInteractions();
  }

  @Test
  void testQueryMessageByUUIDReturnImage() throws Exception {
    String SELECT = "SELECT * FROM message WHERE uuid=(?)";
    /* Stub */
    when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    when(pstmt.executeQuery()).thenReturn(rs);
    when(rs.getString("uuid")).thenReturn(TEST_UUID);
    when(rs.getString("username")).thenReturn(TEST_USERNAME);
    when(rs.getString("content")).thenReturn(TEST_CONTENT);
    when(rs.getString("time")).thenReturn(TEST_TIME);
    when(rs.getBoolean("withImage")).thenReturn(true);
    when(rs.getString("path")).thenReturn(TEST_PATH);
    when(rs.next()).thenReturn(true, false); /* Only one item in ResultSet */
    /* Test return value */
    List<Message> resultList = messageDAO.queryMessageByUUID(UUID.fromString(TEST_UUID));
    assertEquals(1, resultList.size());
    assertEquals(UUID.fromString(TEST_UUID), resultList.get(0).get_uuid());
    assertEquals(TEST_USERNAME, resultList.get(0).get_username());
    assertEquals(TEST_CONTENT, resultList.get(0).get_content());
    assertEquals(TEST_TIME, dateFormatter.format(resultList.get(0).get_time()));
    assertEquals(TEST_PATH, resultList.get(0).get_path());
    /* Test function calls' order */
    InOrder order = inOrder(connection, pstmt, rs);
    order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
    order.verify(pstmt).executeQuery();
    order.verify(rs).next();
    order.verify(rs, times(4)).getString(anyString());
    order.verify(rs).getBoolean(anyString());
    order.verify(rs).getString(anyString());
    order.verify(pstmt).close();
    order.verify(connection).close();
    order.verifyNoMoreInteractions();
  }

  @Test
  void testQueryMessageByNullUUIDThrowsException() {
    assertThrows(NullPointerException.class, () -> messageDAO.queryMessageByUUID(null));
  }

  @Test
  void testQueryMessageByUUIDThrowsSQLExceptionWhenPreparingStatement() throws Exception {
    /* Stub */
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
    /* Test return value */
    List<Message> resultList = messageDAO.queryMessageByUUID(UUID.fromString(TEST_UUID));
    assertTrue(resultList.isEmpty()); /* resultList should be empty */
    /* Test error output */
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
  }

  @Test
  void testQueryMessageByUUIDThrowsSQLExceptionWhenExecutingQuery() throws Exception {
    /* Stub */
    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
    when(pstmt.executeQuery()).thenThrow(SQLException.class);
    /* Test return value */
    List<Message> resultList = messageDAO.queryMessageByUUID(UUID.fromString(TEST_UUID));
    assertTrue(resultList.isEmpty()); /* resultList should be empty */
    /* Test error output */
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
  }

  @Test
  void testQueryMessageWithSize1AndMillisec1572364800000WithImage() throws Exception {
    String SELECT = "SELECT TOP 1 * FROM message WHERE time <= ? ORDER BY time DESC";
    /* Stub */
    when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    when(pstmt.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(true, false); /* rs has only 1 item */
    when(rs.getString("uuid")).thenReturn(TEST_UUID);
    when(rs.getString("username")).thenReturn(TEST_USERNAME);
    when(rs.getString("content")).thenReturn(TEST_CONTENT);
    when(rs.getString("time")).thenReturn(TEST_TIME);
    when(rs.getBoolean("withImage")).thenReturn(true);
    when(rs.getString("path")).thenReturn(TEST_PATH);
    /* Test return value */
    List<Message> resultList = messageDAO.queryMessage(1, TEST_MILLISEC);
    assertEquals(1, resultList.size());
    assertEquals(UUID.fromString(TEST_UUID), resultList.get(0).get_uuid());
    assertEquals(TEST_USERNAME, resultList.get(0).get_username());
    assertEquals(TEST_CONTENT, resultList.get(0).get_content());
    assertEquals(TEST_TIME, dateFormatter.format(resultList.get(0).get_time()));
    /* Test function calls' order */
    InOrder order = inOrder(connection, pstmt, rs);
    order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
    order.verify(pstmt).executeQuery();
    order.verify(rs).next();
    order.verify(rs, times(4)).getString(anyString());
    order.verify(rs).getBoolean(anyString());
    order.verify(rs).getString(anyString());
    order.verify(rs).next();
    order.verify(pstmt).close();
    order.verify(connection).close();
    order.verifyNoMoreInteractions();
  }

  @Test
  void testQueryMessageWithSize2AndMillisec1572364800000WithoutImage() throws Exception {
    String SELECT = "SELECT TOP 2 * FROM message WHERE time <= ? ORDER BY time DESC";
    /* Stub */
    when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    when(pstmt.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(true, true, false); /* rs has 2 items */
    when(rs.getString("uuid")).thenReturn(TEST_UUID, TEST_UUID2);
    when(rs.getString("username")).thenReturn(TEST_USERNAME, TEST_USERNAME2);
    when(rs.getString("content")).thenReturn(TEST_CONTENT, TEST_CONTENT2);
    when(rs.getString("time")).thenReturn(TEST_TIME, TEST_TIME2);
    when(rs.getBoolean("withImage")).thenReturn(false, false);
    /* Test return value */
    List<Message> resultList = messageDAO.queryMessage(2, TEST_MILLISEC);
    assertEquals(2, resultList.size());
    assertEquals(UUID.fromString(TEST_UUID), resultList.get(0).get_uuid());
    assertEquals(TEST_USERNAME, resultList.get(0).get_username());
    assertEquals(TEST_CONTENT, resultList.get(0).get_content());
    assertEquals(TEST_TIME, dateFormatter.format(resultList.get(0).get_time()));
    assertEquals(UUID.fromString(TEST_UUID2), resultList.get(1).get_uuid());
    assertEquals(TEST_USERNAME2, resultList.get(1).get_username());
    assertEquals(TEST_CONTENT2, resultList.get(1).get_content());
    assertEquals(TEST_TIME2, dateFormatter.format(resultList.get(1).get_time()));
    /* Test function calls' order */
    InOrder order = inOrder(connection, pstmt, rs);
    order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
    order.verify(pstmt).executeQuery();
    order.verify(rs).next();
    order.verify(rs, times(4)).getString(anyString());
    order.verify(rs).getBoolean(anyString());
    order.verify(rs).next();
    order.verify(rs, times(4)).getString(anyString());
    order.verify(rs).getBoolean(anyString());
    order.verify(rs).next();
    order.verify(pstmt).close();
    order.verify(connection).close();
    order.verifyNoMoreInteractions();
  }

  @Test
  void testQueryMessageThrowsSQLException() throws Exception {
    /* Stub */
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
    /* Test return value */
    List<Message> result = messageDAO.queryMessage(0, 0);
    assertEquals(0, result.size());
    /* Test error output */
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
  }

  @Test
  void testQueryUpdates() throws Exception {
    String SELECT = "SELECT COUNT(*) FROM message WHERE time > ?";
    /* Stub */
    when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    when(pstmt.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(true);
    when(rs.getInt(1)).thenReturn(3);
    /* Test return value */
    int result = messageDAO.queryUpdates(TEST_MILLISEC);
    assertEquals(3, result);
    /* Test function calls' order */
    InOrder order = inOrder(connection, pstmt, rs);
    order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
    order.verify(pstmt).executeQuery();
    order.verify(rs).next();
    order.verify(rs).getInt(1);
    order.verify(pstmt).close();
    order.verify(connection).close();
    order.verifyNoMoreInteractions();
  }

  @Test
  void testQueryUpdatesThrowsSQLException() throws Exception {
    /* Stub */
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
    /* Test return value */
    int result = messageDAO.queryUpdates(TEST_MILLISEC);
    assertEquals(0, result);
    /* Test error output */
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
  }

  @Test
  void testQueryUpdatesWhenResultSetHasNothing() throws Exception {
    String SELECT = "SELECT COUNT(*) FROM message WHERE time > ?";
    /* Stub */
    when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    when(pstmt.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(false);
    /* Test return value */
    int result = messageDAO.queryUpdates(TEST_MILLISEC);
    assertEquals(0, result);
    /* Test function calls' order */
    InOrder order = inOrder(connection, pstmt, rs);
    order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
    order.verify(pstmt).executeQuery();
    order.verify(rs).next();
    order.verify(pstmt).close();
    order.verify(connection).close();
    order.verifyNoMoreInteractions();
  }

  @Test
  void testClearTable() throws Exception {
    String DELETE = "DELETE FROM message";
    /* Stub */
    when(connection.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    /* Test return value */
    boolean result = messageDAO.clearTable();
    assertTrue(result);
    /* Test function calls' order */
    InOrder order = inOrder(connection, pstmt);
    order.verify(connection).prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS);
    order.verify(pstmt).execute();
    order.verify(pstmt).close();
    order.verify(connection).close();
    order.verifyNoMoreInteractions();
  }

  @Test
  void testClearTableThrowsSQLException() throws Exception {
    /* Stub */
    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
    messageDAO.clearTable();
    /* Test error output */
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
  }

  @Test
  void testCloseStatementThrowsSQLException() throws Exception {
    /* Stub */
    doThrow(SQLException.class).when(pstmt).close();
    messageDAO.closeStatementAndConnection(pstmt, connection);
    /* Test error output */
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
  }

  @Test
  void testCloseConnectionThrowsSQLException() throws Exception {
    /* Stub */
    doThrow(SQLException.class).when(connection).close();
    messageDAO.closeStatementAndConnection(pstmt, connection);
    /* Test error output */
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
  }

  @Test
  void testCloseNullStatement() {
    assertDoesNotThrow(() -> messageDAO.closeStatementAndConnection(null, connection));
  }

  @Test
  void testCloseNullConnection() {
    assertDoesNotThrow(() -> messageDAO.closeStatementAndConnection(pstmt, null));
  }
}
