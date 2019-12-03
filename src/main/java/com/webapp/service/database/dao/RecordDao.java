package com.webapp.service.database.dao;

import com.webapp.model.Build;
import com.webapp.model.Record;
import java.sql.Connection;
import java.util.List;

public interface RecordDao {
  List<Record> listRecord(int size);
  List<Record> listRecordWithBuildId(int size, int buildId);
  List<Record> listRecordWithUserId(int size, int userId);
  Record queryRecordById(int id);
  boolean insertRecord(Record record);
  boolean deleteRecordById(int id);
  boolean verifyRecordById(int id);

}
