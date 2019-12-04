package com.webapp.service.database.dao.impl;

import com.webapp.model.Building;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.BuildingDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements BuildingDao interface to interact with table 't_building' in database.
 *
 * @author Juntao Peng (original creator)
 * @author Shangzhen Li (refactor)
 */
public class BuildingDaoImpl extends DatabaseService implements BuildingDao {
  private Connection connection;

  public BuildingDaoImpl() {
    this.connection = getConnection();
  }

  @Override
  public List<Building> listBuilding(int size) {
    List<Building> buildingList = new ArrayList<>();
    String SQL = "SELECT * FROM t_building LIMIT ?";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(SQL);
      preparedStatement.setInt(1, size);
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        Building building = new Building();
        building.setId(rs.getInt("id"));
        building.setName(rs.getString("name"));
        building.setDescription(rs.getString("description"));
        building.setPrice(rs.getString("price"));
        buildingList.add(building);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return buildingList;
  }

  @Override
  public Building queryBuildingById(int id) {
    String sql = "SELECT * FROM t_building t1 WHERE t1.id=?";
    Building building = new Building();
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        building.setId(rs.getInt("id"));
        building.setName(rs.getString("name"));
        building.setDescription(rs.getString("description"));
        building.setPrice(rs.getString("price"));
        return building;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public boolean addBuilding(Building building) {
    String sql = "INSERT INTO t_building(name,description,price) VALUES (?,?,?)";
    int result = 0;
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, building.getName());
      pstmt.setString(2, building.getDescription());
      pstmt.setString(3, building.getPrice());
      result = pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    if (result != 0) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean deleteBuilding(int id) {
    String sql = "DELETE FROM t_building WHERE id=?";
    int result = 0;
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, id);
      result = pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    if (result != 0) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean updateBuilding(Building building) {
    String sql = "UPDATE t_building SET name=?,description=?,price=? WHERE id=?";
    int result = 0;
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, building.getName());
      pstmt.setString(2, building.getDescription());
      pstmt.setString(3, building.getPrice());
      pstmt.setInt(4, building.getId());
      result = pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    if (result != 0) {
      return true;
    } else {
      return false;
    }
  }
}