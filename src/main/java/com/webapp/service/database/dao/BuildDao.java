package com.webapp.service.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.webapp.model.Build;
import com.webapp.model.PageBean;
import com.webapp.util.StringUtil;

public class BuildDao {

	public List<Build> buildList(Connection con, PageBean pageBean, Build s_Build)throws Exception {
		List<Build> buildList = new ArrayList<Build>();
		StringBuffer sb = new StringBuffer("select * from t_build t1");
		if(StringUtil.isNotEmpty(s_Build.getBuildName())) {
			sb.append(" where t1.buildName like '%"+ s_Build.getBuildName()+"%'");
		}
		if(pageBean != null) {
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Build build =new Build();
			build.setBuildId(rs.getInt("buildId"));
			build.setBuildName(rs.getString("buildName"));
			build.setDetail(rs.getString("buildDetail"));
			build.setPrice(rs.getString("price"));
			buildList.add(build);
		}
		return buildList;
	}

    public List<Build> buildAllList(Connection con, Build s_Build)throws Exception {
        List<Build> buildList = new ArrayList<Build>();
        StringBuffer sb = new StringBuffer("select * from t_build t1");
        PreparedStatement pstmt = con.prepareStatement(sb.toString());
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            Build build =new Build();
            build.setBuildId(rs.getInt("buildId"));
            build.setBuildName(rs.getString("buildName"));
            build.setDetail(rs.getString("buildDetail"));
            build.setPrice(rs.getString("price"));
            buildList.add(build);
        }
        return buildList;
    }
	
	public static String buildName(Connection con, int buildId)throws Exception {
		String sql = "select * from t_build where buildId=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, buildId);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getString("buildName");
		}
		return null;
	}
	
	public int buildCount(Connection con, Build s_Build)throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) as total from t_build t1");
		if(StringUtil.isNotEmpty(s_Build.getBuildName())) {
			sb.append(" where t1.buildName like '%"+ s_Build.getBuildName()+"%'");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}
	
	public Build buildShow(Connection con, String buildId)throws Exception {
		String sql = "select * from t_build t1 where t1.buildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, buildId);
		ResultSet rs=pstmt.executeQuery();
		Build build = new Build();
		if(rs.next()) {
			build.setBuildId(rs.getInt("buildId"));
			build.setBuildName(rs.getString("buildName"));
			build.setDetail(rs.getString("buildDetail"));
			build.setPrice(rs.getString("price"));
		}
		return build;
	}

	public Build buildShowName(Connection con, String buildName)throws Exception {
		String sql = "select * from t_build t1 where t1.buildName=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, buildName);
		ResultSet rs=pstmt.executeQuery();
		Build build = new Build();
		if(rs.next()) {
			build.setBuildId(rs.getInt("buildId"));
			build.setBuildName(rs.getString("buildName"));
			build.setDetail(rs.getString("buildDetail"));
			build.setPrice(rs.getString("price"));
		}
		return build;
	}
	
	public int buildAdd(Connection con, Build build)throws Exception {
		String sql = "insert into t_build values(null,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, build.getBuildName());
		pstmt.setString(2, build.getDetail());
		pstmt.setString(3, build.getPrice());
		return pstmt.executeUpdate();
	}
	
	public int buildDelete(Connection con, String buildId)throws Exception {
		String sql = "delete from t_build where buildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, buildId);
		return pstmt.executeUpdate();
	}
	
	public int buildUpdate(Connection con, Build build)throws Exception {
		String sql = "update t_build set buildName=?,buildDetail=?,price=? where buildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, build.getBuildName());
		pstmt.setString(2, build.getDetail());
		pstmt.setString(3, build.getPrice());
		pstmt.setInt(4, build.getBuildId());
		return pstmt.executeUpdate();
	}
	
}
