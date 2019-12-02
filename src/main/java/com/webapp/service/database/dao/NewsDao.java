package com.webapp.service.database.dao;

import com.webapp.model.News;
import java.sql.Connection;
import java.util.List;

public interface NewsDao {
  List<News> newsList(Connection con, News s_news);
  List<News> newsListWithBuild(Connection con, News s_news, int buildId);
  List<News> newsListWithNumber(Connection con, News s_news, String studentNumber);
  News newsShow(Connection con, String newsId);
  int newsAdd(Connection con, News news);
  int newsDelete(Connection con, String newsId);
  int newsUpdate(Connection con, News news);
}
