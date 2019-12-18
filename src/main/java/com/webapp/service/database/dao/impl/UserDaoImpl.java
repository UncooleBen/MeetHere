package com.webapp.service.database.dao.impl;

import com.webapp.model.user.Admin;
import com.webapp.model.user.Gender;
import com.webapp.model.user.User;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Juntao Peng
 */
public class UserDaoImpl extends DatabaseService implements UserDao {
    
    @Override
    public List<User> queryAllUsers() {
        Connection connection = getConnection();
        assert connection != null;
        List<User> result = new ArrayList<>();
        String SELECT = "SELECT * FROM t_user ORDER BY id DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            ResultSet rs = preparedStatement.executeQuery();
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
                        Admin admin = new Admin(id, username, password, name, sex, tel);
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
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String sex = rs.getString("sex");
                int permission = rs.getInt("permission");
                String tel = rs.getString("tel");
                switch (permission) {
                    case 0:
                        //result = new Admin(id, username, password, name, sex, tel);
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
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
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

//    @Override
//    public User queryUserByUsername(String username) {
//        Connection connection = getConnection();
//        assert connection != null;
//        assert username != null;
//        assert username.length() > 0;
//        User result = null;
//        String SELECT = "SELECT * FROM t_user WHERE username = ?";
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
//            preparedStatement.setString(1, username);
//            ResultSet rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                int permission = rs.getInt("permission");
//                if (permission > 0) {
//                    int id = rs.getInt("id");
//                    String password = rs.getString("password");
//                    String name = rs.getString("name");
//                    String sex = rs.getString("sex");
//                    String tel = rs.getString("tel");
//                    result = new User(id, username, password, name, sex, tel);
//                }
//            }
//            closeConnection(connection);
//            return result;
//        } catch (SQLException sqlException) {
//            sqlException.printStackTrace(System.err);
//            return result;
//        }
//    }

    @Override
    public List<User> queryUserBySex(Gender gender) {
        Connection connection = getConnection();
        assert connection != null;
        assert gender != null;
        List<User> result = new ArrayList<>();
        String SELECT = "SELECT * FROM t_user WHERE sex=(?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setString(1, gender.toString());
            ResultSet rs = preparedStatement.executeQuery();
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
    public boolean addUser(User user) {
        Connection connection = getConnection();
        assert connection != null;
        assert user != null;
        boolean result = true;
        String INSERT = "INSERT INTO t_user VALUES(null,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getSex().toString());
            preparedStatement.setInt(5, user.getPermission());
            preparedStatement.setString(6, user.getTel());
            preparedStatement.executeUpdate();
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            result = false;
            return result;
        }

    }

    @Override
    public boolean deleteUser(int id) {
        Connection connection = getConnection();
        assert connection != null;
        boolean result = true;
        String DELETE = "DELETE FROM t_user WHERE id=(?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            result = false;
            return result;
        }
    }

    @Override
    public boolean updateUser(User user) {
        Connection connection = getConnection();
        assert connection != null;
        assert user != null;
        assert user.getId() > 0;
        boolean result = true;
        String UPDATE = "UPDATE t_user SET username=(?), password=(?), name=(?), sex=(?), permission=(?), tel=(?) WHERE id=(?)";
        String SELECT = "SELECT * FROM t_user WHERE username=(?) AND password=(?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getSex().toString());
            preparedStatement.setInt(5, user.getPermission());
            preparedStatement.setString(6, user.getTel());
            preparedStatement.setInt(7, user.getId());
            preparedStatement.executeUpdate();
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            result = false;
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
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            closeConnection(connection);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
            return result;
        }
    }

}
