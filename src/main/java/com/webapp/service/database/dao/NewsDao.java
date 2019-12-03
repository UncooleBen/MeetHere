package com.webapp.service.database.dao;

import com.webapp.model.News;
import java.sql.Connection;
import java.util.List;

public interface NewsDao {
  List<News> listNews(int size);
  News queryNewsById(int id);
  boolean insertNews(News news);
  boolean deleteNewsById(int id);
  boolean updateNews(News news);
}
