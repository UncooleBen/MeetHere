package com.webapp.service.database.dao.impl;

import com.webapp.model.News;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.NewsDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Juntao Peng
 */
public class NewsDaoImpl extends DatabaseService implements NewsDao {

    @Override
    public List<News> listNews(int size) {
        Connection connection = getConnection();
        assert connection != null;
        List<News> newsList = new ArrayList<News>();
        String SELECT = "SELECT * FROM t_news ORDER BY last_modified DESC LIMIT ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, size);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                News tempNews = new News(rs.getInt("id"), rs.getString("title"), rs.getLong("created"),
                        rs.getLong("last_modified"), rs.getString("author"), rs.getString("detail"));
                newsList.add(tempNews);
            }
            closeConnection(connection);
            return newsList;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return newsList;
        }
    }

    @Override
    public News queryNewsById(int id) {
        Connection connection = getConnection();
        assert connection != null;
        News result = null;
        String SELECT = "SELECT * FROM t_news WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = new News(rs.getInt("id"), rs.getString("title"), rs.getLong("created"),
                        rs.getLong("last_modified"), rs.getString("author"), rs.getString("detail"));
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }
    }

    @Override
    public boolean insertNews(News news) {
        Connection connection = getConnection();
        assert connection != null;
        assert news != null;
        String INSERT = "INSERT INTO t_news(title, created, last_modified, author, detail) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setLong(2, news.getCreated());
            preparedStatement.setLong(3, news.getLastModified());
            preparedStatement.setString(4, news.getAuthor());
            preparedStatement.setString(5, news.getDetail());
            preparedStatement.execute();
            closeConnection(connection);
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return false;
        }

    }

    @Override
    public boolean deleteNewsById(int id) {
        Connection connection = getConnection();
        assert connection != null;
        assert id > 0;
        String DELETE = "DELETE FROM t_news WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            closeConnection(connection);
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return false;
        }
    }

    @Override
    public boolean updateNews(News news) {
        Connection connection = getConnection();
        assert connection != null;
        assert news != null;
        String UPDATE = "UPDATE t_news SET title = ?, created = ?, last_modified = ?, author = ?, detail = ?" +
                " WHERE id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setLong(2, news.getCreated());
            preparedStatement.setLong(3, news.getLastModified());
            preparedStatement.setString(4, news.getAuthor());
            preparedStatement.setString(5, news.getDetail());
            preparedStatement.setInt(6, news.getId());
            preparedStatement.execute();
            closeConnection(connection);
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return false;
        }
    }
}
