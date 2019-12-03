package com.webapp.service.database.dao.impl;

import com.webapp.model.Buildng;
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
  public List<Buildng> buildList(Buildng s_Buildng) {
    List<Buildng> buildngList = new ArrayList<>();
    StringBuffer sb = new StringBuffer("SELECT * FROM t_build t1");
    if (StringUtil.isNotEmpty(s_Buildng.getBuildName())) {
      sb.append(" WHERE t1.buildName LIKE '%" + s_Buildng.getBuildName() + "%'");
    }
    try {
      PreparedStatement pstmt = connection.prepareStatement(sb.toString());
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        Buildng buildng = new Buildng();
        buildng.setBuildId(rs.getInt("buildId"));
        buildng.setBuildName(rs.getString("buildName"));
        buildng.setDetail(rs.getString("buildDetail"));
        buildng.setPrice(rs.getString("price"));
        buildngList.add(buildng);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return buildngList;
  }

  @Override
  public String buildName(int buildId) {
    String sql = "SELECT * FROM t_building WHERE id=?";
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, buildId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getString("buildName");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public Buildng buildShow(String buildId) {
    String sql = "SELECT * FROM t_building t1 WHERE t1.id=?";
    Buildng buildng = new Buildng();
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, buildId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        buildng.setBuildId(rs.getInt("buildId"));
        buildng.setBuildName(rs.getString("buildName"));
        buildng.setDetail(rs.getString("buildDetail"));
        buildng.setPrice(rs.getString("price"));
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return buildng;
  }

  @Override
  public Buildng buildShowName(String buildName) {
    String sql = "SELECT * FROM t_building t1 WHERE t1.name=?";
    Buildng buildng = new Buildng();
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, buildName);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        buildng.setBuildId(rs.getInt("buildId"));
        buildng.setBuildName(rs.getString("buildName"));
        buildng.setDetail(rs.getString("buildDetail"));
        buildng.setPrice(rs.getString("price"));
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return buildng;
  }

  @Override
  public int buildAdd(Buildng buildng) {
    String sql = "INSERT INTO t_building(name,description,price) VALUES (?,?,?)";
    int result = 0;
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, buildng.getBuildName());
      pstmt.setString(2, buildng.getDetail());
      pstmt.setString(3, buildng.getPrice());
      result = pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return result;
  }

  @Override
  public int buildDelete(String buildId) {
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
  public int buildUpdate(Buildng buildng) {
    String sql = "UPDATE t_building SET name=?,description=?,price=? WHERE id=?";
    int result = 0;
    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, buildng.getBuildName());
      pstmt.setString(2, buildng.getDetail());
      pstmt.setString(3, buildng.getPrice());
      pstmt.setInt(4, buildng.getBuildId());
      result = pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return result;
  }
}
