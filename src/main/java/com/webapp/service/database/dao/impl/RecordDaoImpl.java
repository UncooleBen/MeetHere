package com.webapp.service.database.dao.impl;

import com.webapp.model.Record;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.RecordDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Juntao Peng
 */
public class RecordDaoImpl extends DatabaseService implements RecordDao {

    @Override
    public List<Record> listRecord(int size) {
        Connection connection = getConnection();
        assert connection != null;
        List<Record> recordList = new ArrayList<Record>();
        String SELECT = "SELECT * FROM t_record ORDER BY last_modified DESC LIMIT ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, size);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Record tempRecord = new Record(rs.getInt("id"), rs.getLong("time"), rs.getLong("start_date"),
                        rs.getLong("end_date"), rs.getInt("user_id"), rs.getInt("building_id"),
                        rs.getBoolean("verified"));
                recordList.add(tempRecord);
            }
            closeConnection(connection);
            return recordList;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return recordList;
        }
    }

    @Override
    public List<Record> listRecordWithBuildId(int size, int buildId) {
        Connection connection = getConnection();
        assert connection != null;
        List<Record> recordList = new ArrayList<Record>();
        String SELECT = "SELECT * FROM t_record WHERE building_id = (?) ORDER BY last_modified DESC LIMIT ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, buildId);
            preparedStatement.setInt(2, size);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Record tempRecord = new Record(rs.getInt("id"), rs.getLong("time"), rs.getLong("start_date"),
                        rs.getLong("end_date"), rs.getInt("user_id"), rs.getInt("building_id"),
                        rs.getBoolean("verified"));
                recordList.add(tempRecord);
            }
            closeConnection(connection);
            return recordList;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return recordList;
        }
    }

    @Override
    public List<Record> listRecordWithUserId(int size, int userId) {
        Connection connection = getConnection();
        assert connection != null;
        List<Record> recordList = new ArrayList<Record>();
        String SELECT = "SELECT * FROM t_record WHERE user_id = (?) ORDER BY last_modified DESC LIMIT ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, size);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Record tempRecord = new Record(rs.getInt("id"), rs.getLong("time"), rs.getLong("start_date"),
                        rs.getLong("end_date"), rs.getInt("user_id"), rs.getInt("building_id"),
                        rs.getBoolean("verified"));
                recordList.add(tempRecord);
            }
            closeConnection(connection);
            return recordList;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return recordList;
        }
    }

    @Override
    public Record queryRecordById(int id) {
        Connection connection = getConnection();
        assert connection != null;
        Record result = null;
        String SELECT = "SELECT * FROM t_record WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                result = new Record(rs.getInt("id"), rs.getLong("time"), rs.getLong("start_date"),
                        rs.getLong("end_date"), rs.getInt("user_id"), rs.getInt("building_id"),
                        rs.getBoolean("verified"));
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }
    }

    @Override
    public boolean addRecord(Record record) {
        Connection connection = getConnection();
        assert connection != null;
        assert record != null;
        String INSERT = "INSERT INTO t_record(time, start_date, end_date, user_id, building_id, verified)" +
                " VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setLong(1, record.getTime());
            preparedStatement.setLong(2, record.getStartDate());
            preparedStatement.setLong(3, record.getEndDate());
            preparedStatement.setInt(4, record.getUserId());
            preparedStatement.setInt(5, record.getBuildingId());
            preparedStatement.setBoolean(6, record.isVerified());
            preparedStatement.execute();
            closeConnection(connection);
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return false;
        }
    }

    @Override
    public boolean deleteRecord(int id) {
        Connection connection = getConnection();
        assert connection != null;
        assert id > 0;
        String DELETE = "DELETE FROM t_record WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.execute();
            closeConnection(connection);
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return false;
        }
    }

    @Override
    public boolean updateRecord(Record record) {
        Connection connection = getConnection();
        assert connection != null;
        assert record != null;
        assert record.getId() > 0;
        String UPDATE = "UPDATE t_record SET (time, start_date, end_date, user_id, building_id, verified) WHERE id = ? " +
                "VALUES (?, ?, ?, ?, ?, ?))";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setInt(1, record.getId());
            preparedStatement.setLong(2, record.getTime());
            preparedStatement.setLong(3, record.getStartDate());
            preparedStatement.setLong(4, record.getEndDate());
            preparedStatement.setInt(5, record.getUserId());
            preparedStatement.setInt(6, record.getBuildingId());
            preparedStatement.setBoolean(7, record.isVerified());
            preparedStatement.execute();
            closeConnection(connection);
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return false;
        }
    }
}
