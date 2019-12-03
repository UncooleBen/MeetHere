package com.webapp.service.database.dao.impl;

import com.webapp.model.Building;
import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.BuildingDao;
import com.webapp.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BuildingDaoImpl extends DatabaseService implements BuildingDao {
  private Connection connection;

  public BuildingDaoImpl(Connection connection) {
    this.connection = getConnection();
  }

  @Override
  public List<Building> listBuilding(int size) {
    List<Building> buildingList = new ArrayList<>();
    StringBuffer sb = new StringBuffer("SELECT * FROM t_build t1");
    if (StringUtil.isNotEmpty(s_Buildng.getBuildName())) {
      sb.append(" WHERE t1.buildName LIKE '%" + s_Buildng.getBuildName() + "%'");
    }
    try {
      PreparedStatement pstmt = connection.prepareStatement(sb.toString());
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        Building building = new Building();
        building.setBuildId(rs.getInt("buildId"));
        building.setBuildName(rs.getString("buildName"));
        building.setDetail(rs.getString("buildDetail"));
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
      pstmt.setString(1, buildId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        building.setBuildId(rs.getInt("buildId"));
        building.setBuildName(rs.getString("buildName"));
        building.setDetail(rs.getString("buildDetail"));
        building.setPrice(rs.getString("price"));
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return building;
  }

  @Override
  public boolean addBuilding(Building building){
    String sql = "INSERT INTO t_building(name,description,price) VALUES (?,?,?)";
    int result = 0;
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, building.getBuildName());
      pstmt.setString(2, building.getDetail());
      pstmt.setString(3, building.getPrice());
      result = pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return result;
  }

  @Override
  public boolean deleteBuilding(int id) {
    String sql = "DELETE FROM t_building WHERE id=?";
    int result = 0;
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, buildId);
      result = pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return result;
  }

  @Override
  public boolean updateBuilding(Building building) {
    String sql = "UPDATE t_building SET name=?,description=?,price=? WHERE id=?";
    int result = 0;
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, building.getBuildName());
      pstmt.setString(2, building.getDetail());
      pstmt.setString(3, building.getPrice());
      pstmt.setInt(4, building.getBuildId());
      result = pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return result;
  }
}
