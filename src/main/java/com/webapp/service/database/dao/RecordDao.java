package com.webapp.service.database.dao;

import com.webapp.model.Build;
import com.webapp.model.Record;
import java.sql.Connection;
import java.util.List;

public interface RecordDao {
  List<Record> recordList(Connection con, Record s_record);
  List<Record> recordListWithBuild(Connection con, Record s_record, int buildId);
  List<Record> recordListWithNumber(Connection con, Record s_record, String userNumber);
  List<Build> buildList(Connection con);
  Record recordShow(Connection con, String recordId);
  int recordAdd(Connection con, Record record);
  int recordDelete(Connection con, String recordId);
  int recordUpdate(Connection con, Record record);
}
