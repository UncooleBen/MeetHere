package com.webapp.service.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.webapp.model.user.Admin;
import com.webapp.model.user.Gender;
import com.webapp.model.user.User;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;

/**
 * @author Juntao Peng
 */
public class UserDaoImpl extends DatabaseService implements UserDao {
    
    @Override
    public List<User> queryAllUsers() {
        Connection connection = getConnection();
        assert connection != null;
        List<User> result = new ArrayList<>();
        String SELECT = "SELECT * FROM t_user";
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String sex = rs.getString("sex");
                int permission = rs.getInt("permission");
                String tel = rs.getString("tel");
                switch (permission) {
                    case 0:
                        // Admin admin = new Admin(id, username, password, name, sex, tel);
                        break;
                    case 1:
                        User user = new User(id, username, password, name, sex, tel);
                        result.add(user);
                        break;
                    default:
                        // Left blank
                }
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }
    }

    @Override
    public User queryUserById(int id) {
        Connection connection = getConnection();
        assert connection != null;
        assert id >= 0;
        User result = null;
        String SELECT = "SELECT * FROM t_user WHERE id=(?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String sex = rs.getString("sex");
                int permission = rs.getInt("permission");
                String tel = rs.getString("tel");
                switch (permission) {
                    case 0:
                        result = new Admin(id, username, password, name, sex, tel);
                        break;
                    case 1:
                        result = new User(id, username, password, name, sex, tel);
                        break;
                    default:
                        // Left blank
                }
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }
    }

    @Override
    public List<User> queryUserByName(String name) {
        Connection connection = getConnection();
        assert connection != null;
        assert name != null;
        assert name.length() > 0;
        List<User> result = new ArrayList<>();
        String SELECT = "SELECT * FROM t_user WHERE name=(?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int permission = rs.getInt("permission");
                if (permission > 0) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String _name = rs.getString("name");
                    String sex = rs.getString("sex");
                    String tel = rs.getString("tel");
                    result.add(new User(id, username, password, _name, sex, tel));
                }
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }
    }

    @Override
    public List<User> queryUserBySex(Gender gender) {
        Connection connection = getConnection();
        assert connection != null;
        assert gender != null;
        List<User> result = new ArrayList<>();
        String SELECT = "SELECT * FROM t_user WHERE sex=(?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT);
            pstmt.setString(1, gender.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int permission = rs.getInt("permission");
                if (permission > 0) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String _name = rs.getString("name");
                    String sex = rs.getString("sex");
                    String tel = rs.getString("tel");
                    result.add(new User(id, username, password, _name, sex, tel));
                }
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }
    }

    @Override
    public int addUser(User user) {
        Connection connection = getConnection();
        assert connection != null;
        assert user != null;
        int result = 0;
        String INSERT = "INSERT INTO t_user values(null,?,?,?,?,?,?)";
        String SELECT = "SELECT * FROM t_user WHERE username=(?) AND password=(?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(INSERT);
            pstmt.setString(1, user.get_username());
            pstmt.setString(2, user.get_password());
            pstmt.setString(3, user.get_name());
            pstmt.setString(4, user.get_sex().toString());
            pstmt.setInt(5, user.get_permission());
            pstmt.setString(6, user.get_tel());
            pstmt.executeUpdate();
            // Check whether successes
            pstmt = connection.prepareStatement(SELECT);
            pstmt.setString(1, user.get_username());
            pstmt.setString(2, user.get_password());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("id");
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }

    }

    @Override
    public boolean deleteUser(int id) {
        Connection connection = getConnection();
        assert connection != null;
        assert id > 0;
        boolean result = true;
        String DELETE = "DELETE FROM t_user WHERE id=(?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(DELETE);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            result = false;
            return result;
        }
    }

    @Override
    public int updateUserAll(User user) {
        Connection connection = getConnection();
        assert connection != null;
        assert user != null;
        assert user.get_id() > 0;
        int result = 0;
        String UPDATE = "UPDATE t_user SET username=(?), password=(?), name=(?), sex=(?), permission=(?), tel=(?) WHERE id=(?)";
        String SELECT = "SELECT * FROM t_user WHERE username=(?) AND password=(?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(UPDATE);
            pstmt.setString(1, user.get_username());
            pstmt.setString(2, user.get_password());
            pstmt.setString(3, user.get_name());
            pstmt.setString(4, user.get_sex().toString());
            pstmt.setInt(5, user.get_permission());
            pstmt.setString(6, user.get_tel());
            pstmt.setInt(7, user.get_id());
            pstmt.executeUpdate();
            // Check whether successes
            pstmt = connection.prepareStatement(SELECT);
            pstmt.setString(1, user.get_username());
            pstmt.setString(2, user.get_password());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("id");
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }
    }

    @Override
    public boolean updateUserPassword(int id, String password) {
        Connection connection = getConnection();
        assert connection != null;
        assert id >= 0;
        assert password != null;
        assert password.length() > 0;
        boolean result = false;
        String UPDATE = "UPDATE t_user SET password=(?) WHERE id=(?)";
        String SELECT = "SELECT * FROM t_user WHERE id=(?) AND password=(?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(UPDATE);
            pstmt.setString(1, password);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            // Check whether successes
            pstmt = connection.prepareStatement(SELECT);
            pstmt.setInt(1, id);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = true;
            }
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }
    }

}
