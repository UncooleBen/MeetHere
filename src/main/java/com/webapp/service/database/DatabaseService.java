package com.webapp.service.database;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Juntao Peng
 */
public class DatabaseService {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	@Value("${spring.datasource.driver-class-name}")
	private String dbClassname;


	public Connection getConnection() {
		try {
			Class.forName(dbClassname);
			Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			return connection;
		} catch (ClassNotFoundException classNotFoundException) {
			classNotFoundException.printStackTrace(System.err);
			return null;
		} catch (SQLException sqlException) {
			sqlException.printStackTrace(System.err);
			return null;
		}
	}

	public boolean closeConnection(Connection connection) {
		if (connection == null) {
			return true;
		}
		try {
			connection.close();
			return true;
		} catch (SQLException sqlException) {
			sqlException.printStackTrace(System.err);
			return false;
		}
	}
}
