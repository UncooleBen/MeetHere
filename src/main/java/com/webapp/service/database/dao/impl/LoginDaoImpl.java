package com.webapp.service.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.webapp.model.user.Admin;
import com.webapp.model.user.User;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juntao Peng
 */
public class LoginDaoImpl extends DatabaseService implements LoginDao {

	@Override
	public User login(String loginUsername, String loginPassword) {
		Connection connection = getConnection();
		assert connection != null;
		assert loginUsername != null;
		assert loginPassword != null;

		User result_user = null;
		String SELECT = "SELECT * FROM t_user WHERE username=(?) AND password=(?)";
		try {
			PreparedStatement pstmt = connection.prepareStatement(SELECT);
			pstmt.setString(1, loginUsername);
			pstmt.setString(2, loginPassword);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String sex = rs.getString("sex");
				int permission = rs.getInt("permission");
				String tel = rs.getString("tel");
				switch (permission) {
				case 0:
					result_user = new Admin(id, username, password, name, sex, tel);
					break;
				case 1:
					result_user = new User(id, username, password, name, sex, tel);
					break;
				default:
					// Empty
				}
			}
			closeConnection(connection);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace(System.err);
		}
		return result_user;
	}

	@Override
	public User signup(User user) {
		Connection connection = getConnection();
		assert connection != null;
		assert user != null;

		User result_user = null;
		String SELECT = "SELECT * FROM t_user WHERE username=(?)";
		try {
			PreparedStatement pstmt = connection.prepareStatement(SELECT);
			pstmt.setString(1, user.get_username());
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				UserDaoImpl userDao = new UserDaoImpl();
				int id = userDao.addUser(user);
				user.set_id(id);
				result_user = user;
			}
			closeConnection(connection);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace(System.err);
		}
		return result_user;
	}

}
