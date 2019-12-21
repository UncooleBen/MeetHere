package com.webapp.service.database.dao.impl;

import com.webapp.model.user.Admin;
import com.webapp.model.user.User;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.LoginDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        User resultUser = null;
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
                        resultUser = new Admin(id, username, password, name, sex, tel);
                        break;
                    case 1:
                        resultUser = new User(id, username, password, name, sex, tel);
                        break;
                    default:
                        // Empty
                }
            }
            closeConnection(connection);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
        }
        return resultUser;
    }

    @Override
    public User signup(User user) {
        Connection connection = getConnection();

        User resultUser = null;
        String SELECT = "SELECT * FROM t_user WHERE username = ?";
        String INSERT = "INSERT INTO t_user (username, password, name, sex, permission, tel) " +
                "VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT);
            pstmt.setString(1, user.getUsername());
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                pstmt = connection.prepareStatement(INSERT);
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getSex().toString());
                pstmt.setInt(5, 1);
                pstmt.setString(6, user.getTel());
                pstmt.execute();
                resultUser = user;
            }
            closeConnection(connection);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
        }
        return resultUser;
    }

}
