package com.webapp.service.database.dao;

import com.webapp.model.News;

import java.util.List;

/**
 * This interface declares methods used to interact with table 't_news' in the database.
 *
 * @author Juntao Peng
 */
public interface NewsDao {
    /**
     * Get a limited number of news records from database.
     *
     * @param size The wanted size of records
     * @return A List<News> contains news records
     */
    List<News> listNews(int size);

    /**
     * Query news record from database given a news id.
     *
     * @param id The news id
     * @return A News object if query hits, otherwise null
     */
    News queryNewsById(int id);

    /**
     * Insert a news record into the database.
     *
     * @param news The News wanted to insert
     * @return True if insertion succeeded, otherwise false
     */
    boolean insertNews(News news);

    /**
     * Delete a news record in the database.
     *
     * @param id The news id
     * @return True if insertion succeeded, otherwise false
     */
    boolean deleteNewsById(int id);

    /**
     * Update a news record into the database.
     *
     * @param news The News wanted to insert
     * @return True if insertion succeeded, otherwise false
     */
    boolean updateNews(News news);

}
