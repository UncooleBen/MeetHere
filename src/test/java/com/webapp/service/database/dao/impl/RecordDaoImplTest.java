package com.webapp.service.database.dao.impl;


import com.webapp.model.Record;

import com.webapp.service.database.dao.RecordDao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class RecordDaoImplTest {
    private RecordDao recordDao;
    private Connection connection = mock(Connection.class);
    private PreparedStatement preparedStatement = mock(PreparedStatement.class);
    private SQLException test_sql_exception;
    private ResultSet rs = mock(ResultSet.class);
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;



    class TestableRecordDaoImpl extends RecordDaoImpl
    {
        @Override
        public Connection getConnection() {
            return connection;
        }
    }

    @BeforeEach
    void init() {
        this.recordDao = new RecordDaoImplTest.TestableRecordDaoImpl();
        this.test_sql_exception = new SQLException();
        this.outContent = new ByteArrayOutputStream();
        this.errContent = new ByteArrayOutputStream();
        this.originalOut = System.out;
        this.originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tear_down() throws IOException {
        System.setErr(this.originalErr);
        System.setOut(this.originalOut);
        this.outContent.close();
        this.errContent.close();
    }


    @Test
    void test_throws_sql_exception_when_list_record() throws SQLException
    {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.recordDao.listRecord(5,true);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_when_list_record() throws SQLException
    {
        int id=305;
        long time=4456;
        long start_date=44556;
        long end_date=45454;
        int user_id=305;
        int building_id=154;
        boolean verified=true;

        Record record = new Record(id,time,start_date,end_date,user_id,building_id,verified);

        List<Record> recordList = new ArrayList<>();
        recordList.add(record);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
        when(rs.getInt("id")).thenReturn(id);
        when(rs.getLong("time")).thenReturn(time);
        when(rs.getLong("start_date")).thenReturn(start_date);
        when(rs.getLong("end_date")).thenReturn(end_date);
        when(rs.getInt("user_id")).thenReturn(user_id);
        when(rs.getInt("building_id")).thenReturn(building_id);
        when(rs.getBoolean("verified")).thenReturn(verified);




        assertEquals(recordList,this.recordDao.listRecord(5,true));
        verify(preparedStatement).setBoolean(1,verified);
        verify(preparedStatement).setInt(2,5);

    }

    @Test
    void test_throws_sql_exception_when_list_record_with_UserId() throws SQLException
    {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.recordDao.listRecordWithUserId(5,305,true);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_list_record_with_UserId() throws SQLException
    {
        int id=305;
        long time=4456;
        long start_date=44556;
        long end_date=45454;
        int user_id=305;
        int building_id=154;
        boolean verified=true;

        Record record = new Record(id,time,start_date,end_date,user_id,building_id,verified);

        List<Record> recordList = new ArrayList<>();
        recordList.add(record);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
        when(rs.getInt("id")).thenReturn(id);
        when(rs.getLong("time")).thenReturn(time);
        when(rs.getLong("start_date")).thenReturn(start_date);
        when(rs.getLong("end_date")).thenReturn(end_date);
        when(rs.getInt("user_id")).thenReturn(user_id);
        when(rs.getInt("building_id")).thenReturn(building_id);
        when(rs.getBoolean("verified")).thenReturn(verified);

        this.recordDao.listRecordWithUserId(5,305,true);

        verify(preparedStatement).setInt(1,305);
        verify(preparedStatement).setInt(2,5);
    }




    @Test
    void test_throws_sql_exception_when_query_record_by_id() throws SQLException
    {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.recordDao.queryRecordById(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_query_Record_By_Id() throws SQLException
    {
        int id=305;
        long time=4456;
        long start_date=44556;
        long end_date=45454;
        int user_id=305;
        int building_id=154;
        boolean verified=true;

        Record record = new Record(id,time,start_date,end_date,user_id,building_id,verified);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
        when(rs.getInt("id")).thenReturn(id);
        when(rs.getLong("time")).thenReturn(time);
        when(rs.getLong("start_date")).thenReturn(start_date);
        when(rs.getLong("end_date")).thenReturn(end_date);
        when(rs.getInt("user_id")).thenReturn(user_id);
        when(rs.getInt("building_id")).thenReturn(building_id);
        when(rs.getBoolean("verified")).thenReturn(verified);


        assertEquals(record,this.recordDao.queryRecordById(5));
        verify(preparedStatement).setInt(1,5);
    }

    @Test
    void test_throws_sql_exception_when_add_Record() throws SQLException
    {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.recordDao.addRecord(new Record());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_add_Record_When_Result_Is_True() throws SQLException
    {
        int id=305;
        long time=4456;
        long start_date=44556;
        long end_date=45454;
        int user_id=305;
        int building_id=154;
        boolean verified=true;

        Record record = new Record(id,time,start_date,end_date,user_id,building_id,verified);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        boolean result=this.recordDao.addRecord(record);

        assertEquals(result,true);
        verify(preparedStatement).execute();
        verify(preparedStatement).setLong(1,time);
        verify(preparedStatement).setLong(2,start_date);
        verify(preparedStatement).setLong(3,end_date);
        verify(preparedStatement).setInt(4,user_id);
        verify(preparedStatement).setInt(5,building_id);
        verify(preparedStatement).setBoolean(6,verified);



    }


    @Test
    void test_delete_Record_When_Result_Is_True() throws SQLException
    {

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        boolean result=this.recordDao.deleteRecord(5);

        assertEquals(result,true);
        verify(preparedStatement).execute();
        verify(preparedStatement).setInt(1,5);
    }


    @Test
    void test_throws_sql_exception_when_deleteRecord() throws SQLException
    {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.recordDao.deleteRecord(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_throws_sql_exception_when_updateRecord() throws SQLException
    {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.recordDao.updateRecord(new Record());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_update_Record_When_Result_Is_True() throws SQLException
    {
        int id=305;
        long time=4456;
        long start_date=44556;
        long end_date=45454;
        int user_id=305;
        int building_id=154;
        boolean verified=true;

        Record record = new Record(id,time,start_date,end_date,user_id,building_id,verified);



        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result=this.recordDao.updateRecord(record);

        assertEquals(result,true);

        verify(preparedStatement).execute();
        verify(preparedStatement).setLong(1,time);
        verify(preparedStatement).setLong(2,start_date);
        verify(preparedStatement).setLong(3,end_date);
        verify(preparedStatement).setInt(4,user_id);
        verify(preparedStatement).setInt(5,building_id);
        verify(preparedStatement).setBoolean(6,verified);
        verify(preparedStatement).setInt(7,id);

    }
}