package com.webapp.service.database.dao.impl;

import com.webapp.model.Record;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Juntao Peng
 */
class RecordDaoImplIT {
    RecordDaoImpl recordDao;
    Connection connection;

    @BeforeEach
    void init() {
        this.recordDao = new RecordDaoImpl();
        String dbUrl = "jdbc:mysql://localhost:3306/meethere?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
        String dbUsername = "root";
        String dbPassword = "root";
        String dbClassname = "com.mysql.jdbc.Driver";
        ReflectionTestUtils.setField(this.recordDao, "dbUrl", dbUrl);
        ReflectionTestUtils.setField(this.recordDao, "dbUsername", dbUsername);
        ReflectionTestUtils.setField(this.recordDao, "dbPassword", dbPassword);
        ReflectionTestUtils.setField(this.recordDao, "dbClassname", dbClassname);
        this.connection = null;
    }

    @AfterEach
    void tearDown() {
        this.recordDao.closeConnection(this.connection);
    }

    @Test
    void addAVerifiedRecordAndQueryItByList() {
        long time = System.currentTimeMillis();
        Record record = new Record(time, time, time, 1, 1, true);
        this.recordDao.addRecord(record);
        Record insertedRecord = this.recordDao.listRecord(1, true).get(0);
        assertAll(
                () -> assertEquals(record.getTime(), insertedRecord.getTime()),
                () -> assertEquals(record.getStartDate(), insertedRecord.getStartDate()),
                () -> assertEquals(record.getEndDate(), insertedRecord.getEndDate()),
                () -> assertEquals(record.getUserId(), insertedRecord.getUserId()),
                () -> assertEquals(record.getBuildingId(), insertedRecord.getBuildingId()));
        this.recordDao.deleteRecord(insertedRecord.getId());
    }

    @Test
    void addAnUnverifiedRecordAndQueryItByList() {
        long time = System.currentTimeMillis();
        Record record = new Record(time, time, time, 1, 1, false);
        this.recordDao.addRecord(record);
        Record insertedRecord = this.recordDao.listRecord(1, false).get(0);
        assertAll(
                () -> assertEquals(record.getTime(), insertedRecord.getTime()),
                () -> assertEquals(record.getStartDate(), insertedRecord.getStartDate()),
                () -> assertEquals(record.getEndDate(), insertedRecord.getEndDate()),
                () -> assertEquals(record.getUserId(), insertedRecord.getUserId()),
                () -> assertEquals(record.getBuildingId(), insertedRecord.getBuildingId()));
        this.recordDao.deleteRecord(insertedRecord.getId());
    }

    @Test
    void addAVerifiedRecordAndQueryItById() {
        long time = System.currentTimeMillis();
        Record record = new Record(time, time, time, 1, 1, true);
        this.recordDao.addRecord(record);
        Record insertedRecord = this.recordDao.queryRecordById(this.recordDao.listRecord(1, true).get(0).getId());
        assertAll(
                () -> assertEquals(record.getTime(), insertedRecord.getTime()),
                () -> assertEquals(record.getStartDate(), insertedRecord.getStartDate()),
                () -> assertEquals(record.getEndDate(), insertedRecord.getEndDate()),
                () -> assertEquals(record.getUserId(), insertedRecord.getUserId()),
                () -> assertEquals(record.getBuildingId(), insertedRecord.getBuildingId()));
        this.recordDao.deleteRecord(insertedRecord.getId());
    }

    @Test
    void addAnUnverifiedRecordAndQueryItById() {
        long time = System.currentTimeMillis();
        Record record = new Record(time, time, time, 1, 1, false);
        this.recordDao.addRecord(record);
        Record insertedRecord = this.recordDao.queryRecordById(this.recordDao.listRecord(1, false).get(0).getId());
        assertAll(() -> assertEquals(record.getTime(), insertedRecord.getTime()),
                () -> assertEquals(record.getStartDate(), insertedRecord.getStartDate()),
                () -> assertEquals(record.getEndDate(), insertedRecord.getEndDate()),
                () -> assertEquals(record.getUserId(), insertedRecord.getUserId()),
                () -> assertEquals(record.getBuildingId(), insertedRecord.getBuildingId()));
        this.recordDao.deleteRecord(insertedRecord.getId());
    }

    @Test
    void add20VerifiedRecordAndQueryByList() {
        List<Record> recordList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 19; i >= 0; i--) {
            recordList.add(new Record(time + i, time + i, time + i, 1 + i, 1 + i, true));
        }
        for (Record record : recordList) {
            this.recordDao.addRecord(record);
        }
        List<Record> resultList = this.recordDao.listRecord(20, true);
        for (int i = 0; i < 20; i++) {
            Record record = recordList.get(i);
            Record insertedRecord = resultList.get(i);
            assertAll(
                    () -> assertEquals(record.getTime(), insertedRecord.getTime()),
                    () -> assertEquals(record.getStartDate(), insertedRecord.getStartDate()),
                    () -> assertEquals(record.getEndDate(), insertedRecord.getEndDate()),
                    () -> assertEquals(record.getUserId(), insertedRecord.getUserId()),
                    () -> assertEquals(record.getBuildingId(), insertedRecord.getBuildingId()));
        }
        for (int i = 0; i < 20; i++) {
            Record record = resultList.get(i);
            this.recordDao.deleteRecord(record.getId());
        }
    }

    @Test
    void add20UnverifiedRecordAndQueryByList() {
        List<Record> recordList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 19; i >= 0; i--) {
            recordList.add(new Record(time + i, time + i, time + i, 1 + i, 1 + i, false));
        }
        for (Record record : recordList) {
            this.recordDao.addRecord(record);
        }
        List<Record> resultList = this.recordDao.listRecord(20, false);
        for (int i = 0; i < 20; i++) {
            Record record = recordList.get(i);
            Record insertedRecord = resultList.get(i);
            assertAll(
                    () -> assertEquals(record.getTime(), insertedRecord.getTime()),
                    () -> assertEquals(record.getStartDate(), insertedRecord.getStartDate()),
                    () -> assertEquals(record.getEndDate(), insertedRecord.getEndDate()),
                    () -> assertEquals(record.getUserId(), insertedRecord.getUserId()),
                    () -> assertEquals(record.getBuildingId(), insertedRecord.getBuildingId()));
        }
        for (int i = 0; i < 20; i++) {
            Record record = resultList.get(i);
            this.recordDao.deleteRecord(record.getId());
        }
    }

    @Test
    void add20VerifiedRecordAndUpdateToUnverified() {
        List<Record> recordList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 19; i >= 0; i--) {
            recordList.add(new Record(time + i, time + i, time + i, 1 + i, 1 + i, true));
        }
        for (Record record : recordList) {
            this.recordDao.addRecord(record);
        }
        List<Record> resultList = this.recordDao.listRecord(20, true);
        for (int i = 0; i < 20; i++) {
            Record record = resultList.get(i);
            record.setTime(time * 2 + i);
            record.setStartDate(time * 2 + i);
            record.setEndDate(time * 2 + i);
            record.setVerified(false);
            record.setBuildingId(2 + i);
            record.setUserId(2 + i);
            this.recordDao.updateRecord(record);
        }
        List<Record> updatedResultList = this.recordDao.listRecord(20, false);
        for (int i = 0; i < 20; i++) {
            Record record = resultList.get(20 - i - 1);
            Record insertedRecord = updatedResultList.get(i);
            assertAll(
                    () -> assertEquals(record.getTime(), insertedRecord.getTime()),
                    () -> assertEquals(record.getStartDate(), insertedRecord.getStartDate()),
                    () -> assertEquals(record.getEndDate(), insertedRecord.getEndDate()),
                    () -> assertEquals(record.getUserId(), insertedRecord.getUserId()),
                    () -> assertEquals(record.getBuildingId(), insertedRecord.getBuildingId()));
        }
        for (int i = 0; i < 20; i++) {
            Record record = updatedResultList.get(i);
            this.recordDao.deleteRecord(record.getId());
        }
    }

    @Test
    void add20UnverifiedRecordAndUpdateToVerified() {
        List<Record> recordList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 19; i >= 0; i--) {
            recordList.add(new Record(time + i, time + i, time + i, 1 + i, 1 + i, false));
        }
        for (Record record : recordList) {
            this.recordDao.addRecord(record);
        }
        List<Record> resultList = this.recordDao.listRecord(20, false);
        for (int i = 0; i < 20; i++) {
            Record record = resultList.get(i);
            record.setTime(time * 2 + i);
            record.setStartDate(time * 2 + i);
            record.setEndDate(time * 2 + i);
            record.setVerified(true);
            record.setBuildingId(2 + i);
            record.setUserId(2 + i);
            this.recordDao.updateRecord(record);
        }
        List<Record> updatedResultList = this.recordDao.listRecord(20, true);
        for (int i = 0; i < 20; i++) {
            Record record = resultList.get(20 - i - 1);
            Record insertedRecord = updatedResultList.get(i);
            assertAll(
                    () -> assertEquals(record.getTime(), insertedRecord.getTime()),
                    () -> assertEquals(record.getStartDate(), insertedRecord.getStartDate()),
                    () -> assertEquals(record.getEndDate(), insertedRecord.getEndDate()),
                    () -> assertEquals(record.getUserId(), insertedRecord.getUserId()),
                    () -> assertEquals(record.getBuildingId(), insertedRecord.getBuildingId()));
        }
        for (int i = 0; i < 20; i++) {
            Record record = updatedResultList.get(i);
            this.recordDao.deleteRecord(record.getId());
        }
    }

}