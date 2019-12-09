package com.webapp.service.database.dao;

import com.webapp.model.Record;

import java.util.List;


/**
 * This interface declares methods used to interact with table 't_record' in the database.
 *
 * @author Juntao Peng
 */
public interface RecordDao {

    /**
     * Get the list of records in 't_record'.
     *
     * @param size The size of record list
     * @return A list of record
     */
    List<Record> listRecord(int size, boolean verified);

    /**
     * Get the list of records in 't_record' given building id.
     *
     * @param size    The size of record list
     * @param buildId The building id
     * @return A list of record
     */
    List<Record> listRecordWithBuildId(int size, int buildId);

    /**
     * Get the list of records in 't_record' given user id.
     *
     * @param size   The size of record list
     * @param userId The user id
     * @return A list of record
     */
    List<Record> listRecordWithUserId(int size, int userId);

    /**
     * Query record given its id.
     *
     * @param id The record id
     * @return A record if succeeded, otherwise null
     */
    Record queryRecordById(int id);

    /**
     * Add a record in 't_record'.
     *
     * @param record The record to add
     * @return True if succeeded, otherwise false
     */
    boolean addRecord(Record record);

    /**
     * Delete a record in 't_user' given its id
     *
     * @param id The record id
     * @return True if succeeded, otherwise false
     */
    boolean deleteRecord(int id);

    /**
     * Update a record in 't_user' given its id
     *
     * @param record The record to update
     * @return True if succeeded, otherwise false
     */
    boolean updateRecord(Record record);

}
