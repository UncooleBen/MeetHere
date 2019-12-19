package com.webapp.service.database.dao.impl;

import com.webapp.model.News;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NewsDaoImplIT {

    NewsDaoImpl newsDao;
    Connection connection;

    @BeforeEach
    void init() {
        this.newsDao = new NewsDaoImpl();
        String dbUrl = "jdbc:mysql://localhost:3306/meethere?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
        String dbUsername = "root";
        String dbPassword = "root";
        String dbClassname = "com.mysql.jdbc.Driver";
        ReflectionTestUtils.setField(this.newsDao, "dbUrl", dbUrl);
        ReflectionTestUtils.setField(this.newsDao, "dbUsername", dbUsername);
        ReflectionTestUtils.setField(this.newsDao, "dbPassword", dbPassword);
        ReflectionTestUtils.setField(this.newsDao, "dbClassname", dbClassname);
        this.connection = null;
    }

    @AfterEach
    void tearDown() {
        this.newsDao.closeConnection(this.connection);
    }

    @Test
    void addANewsAndQueryItByList() {
        long time = System.currentTimeMillis();
        News news = new News("title", time, time, "author", "detail");
        this.newsDao.insertNews(news);
        News insertedNews = this.newsDao.listNews(1).get(0);
        assertAll(
                () -> assertEquals(news.getAuthor(), insertedNews.getAuthor()),
                () -> assertEquals(news.getCreated(), insertedNews.getCreated()),
                () -> assertEquals(news.getDetail(), insertedNews.getDetail()),
                () -> assertEquals(news.getLastModified(), insertedNews.getLastModified()),
                () -> assertEquals(news.getTitle(), insertedNews.getTitle()));
        this.newsDao.deleteNewsById(insertedNews.getId());
    }

    @Test
    void addANewsAndQueryItById() {
        long time = System.currentTimeMillis();
        News news = new News("title", time, time, "author", "detail");
        this.newsDao.insertNews(news);
        News insertedNews = this.newsDao.queryNewsById(this.newsDao.listNews(1).get(0).getId());
        assertAll(
                () -> assertEquals(news.getAuthor(), insertedNews.getAuthor()),
                () -> assertEquals(news.getCreated(), insertedNews.getCreated()),
                () -> assertEquals(news.getDetail(), insertedNews.getDetail()),
                () -> assertEquals(news.getLastModified(), insertedNews.getLastModified()),
                () -> assertEquals(news.getTitle(), insertedNews.getTitle()));
        this.newsDao.deleteNewsById(insertedNews.getId());
    }

    @Test
    void add20NewsAndQueryByList() {
        List<News> newsList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 19; i >= 0; i--) {
            newsList.add(new News("title" + i, time + i, time + i, "author" + i, "detail" + i));
        }
        for (News news : newsList) {
            this.newsDao.insertNews(news);
        }
        List<News> resultList = this.newsDao.listNews(20);
        for (int i = 0; i < 20; i++) {
            News news = newsList.get(i);
            News insertedNews = resultList.get(i);
            assertAll(
                    () -> assertEquals(news.getAuthor(), insertedNews.getAuthor()),
                    () -> assertEquals(news.getCreated(), insertedNews.getCreated()),
                    () -> assertEquals(news.getDetail(), insertedNews.getDetail()),
                    () -> assertEquals(news.getLastModified(), insertedNews.getLastModified()),
                    () -> assertEquals(news.getTitle(), insertedNews.getTitle()));
        }
        for (int i = 0; i < 20; i++) {
            News news = resultList.get(i);
            this.newsDao.deleteNewsById(news.getId());
        }
    }

    @Test
    void add20NewsAndUpdate() {
        List<News> newsList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 19; i >= 0; i--) {
            newsList.add(new News("title" + i, time + i, time + i, "author" + i, "detail" + i));
        }
        for (News news : newsList) {
            this.newsDao.insertNews(news);
        }
        List<News> resultList = this.newsDao.listNews(20);
        for (int i = 0; i < 20; i++) {
            News news = resultList.get(i);
            news.setAuthor("new author" + i);
            news.setDetail("new detail" + i);
            news.setLastModified(time * 2 + i);
            news.setCreated(time * 2 + i);
            news.setTitle("new title" + i);
            this.newsDao.updateNews(news);
        }
        List<News> updatedResultList = this.newsDao.listNews(20);
        for (int i = 0; i < 20; i++) {
            News news = resultList.get(20 - i - 1);
            News insertedNews = updatedResultList.get(i);
            assertAll(
			() ->            assertEquals(news.getAuthor(), insertedNews.getAuthor()),
			() ->            assertEquals(news.getCreated(), insertedNews.getCreated()),
			() ->            assertEquals(news.getDetail(), insertedNews.getDetail()),
			() ->            assertEquals(news.getLastModified(), insertedNews.getLastModified()),
			() ->            assertEquals(news.getTitle(), insertedNews.getTitle()));
        }
        for (int i = 0; i < 20; i++) {
            News news = updatedResultList.get(i);
            this.newsDao.deleteNewsById(news.getId());
        }
    }

}