package com.webapp.service.database.dao;

import com.webapp.model.Build;
import com.webapp.model.PageBean;
import java.sql.Connection;
import java.util.List;

public interface BuildDao {
  List<Build> buildList(Connection con, PageBean pageBean, Build s_Build);
  List<Build> buildAllList(Connection con, Build s_Build);
  int buildCount(Connection con, Build s_Build);
  Build buildShow(Connection con, String buildId);
  Build buildShowName(Connection con, String buildName);
  int buildAdd(Connection con, Build build);
  int buildDelete(Connection con, String buildId);
  int buildUpdate(Connection con, Build build);
}
