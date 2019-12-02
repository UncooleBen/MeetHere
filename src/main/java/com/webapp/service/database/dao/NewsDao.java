package com.webapp.service.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.webapp.model.News;
import com.webapp.util.StringUtil;

public class NewsDao {
    public List<News> newsList(Connection con, News s_news)throws Exception {
        List<News> newsList = new ArrayList<News>();
        StringBuffer sb = new StringBuffer("select * from t_news t1");
        if(StringUtil.isNotEmpty(s_news.getDate())) {
            sb.append(" and t1.date="+s_news.getDate());
        }
        if(StringUtil.isNotEmpty(s_news.getStartDate())){
            sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_news.getStartDate()+"')");
        }
        if(StringUtil.isNotEmpty(s_news.getEndDate())){
            sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_news.getEndDate()+"')");
        }
        PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            News news=new News();
            news.setNewsId(rs.getInt("newsId"));
            news.setDate(rs.getString("date"));
            news.setAuthor(rs.getString("author"));
            news.setTitle(rs.getString("title"));
            news.setDetail(rs.getString("detail"));
            newsList.add(news);
        }
        return newsList;
    }

    public List<News> newsListWithBuild(Connection con, News s_news, int buildId)throws Exception {
        List<News> newsList = new ArrayList<News>();
        StringBuffer sb = new StringBuffer("select * from t_news t1");
        if(StringUtil.isNotEmpty(s_news.getStartDate())){
            sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_news.getStartDate()+"')");
        }
        if(StringUtil.isNotEmpty(s_news.getEndDate())){
            sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_news.getEndDate()+"')");
        }
        PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            News news=new News();
            news.setNewsId(rs.getInt("newsId"));
            news.setDate(rs.getString("date"));
            news.setDetail(rs.getString("detail"));
            newsList.add(news);
        }
        return newsList;
    }

    public List<News> newsListWithNumber(Connection con, News s_news, String studentNumber)throws Exception {
        List<News> newsList = new ArrayList<News>();
        StringBuffer sb = new StringBuffer("select * from t_news t1");
        if(StringUtil.isNotEmpty(s_news.getStartDate())){
            sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_news.getStartDate()+"')");
        }
        if(StringUtil.isNotEmpty(s_news.getEndDate())){
            sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_news.getEndDate()+"')");
        }
        PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            News news=new News();
            news.setNewsId(rs.getInt("newsId"));
            news.setDate(rs.getString("date"));
            news.setDetail(rs.getString("detail"));
            newsList.add(news);
        }
        return newsList;
    }

    public News newsShow(Connection con, String newsId)throws Exception {
        String sql = "select * from t_news t1 where t1.newsId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, newsId);
        ResultSet rs=pstmt.executeQuery();
        News news = new News();
        if(rs.next()) {
            news.setNewsId(rs.getInt("newsId"));
            news.setDate(rs.getString("date"));
            news.setTitle(rs.getString("title"));
            news.setAuthor(rs.getString("author"));
            news.setDetail(rs.getString("detail"));
        }
        return news;
    }

    public int newsAdd(Connection con, News news)throws Exception {
        String sql = "insert into t_news values(null,?,?,?,?)";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, news.getTitle());
        pstmt.setString(2, news.getDate());
        pstmt.setString(3, news.getAuthor());
        pstmt.setString(4, news.getDetail());
        return pstmt.executeUpdate();
    }

    public int newsDelete(Connection con, String newsId)throws Exception {
        String sql = "delete from t_news where newsId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, newsId);
        return pstmt.executeUpdate();
    }

    public int newsUpdate(Connection con, News news)throws Exception {
        String sql = "update t_news set title=?,date=?,author=?,detail=? where newsId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, news.getTitle());
        pstmt.setString(2, news.getDate());
        pstmt.setString(3, news.getAuthor());
        pstmt.setString(4, news.getDetail());
        pstmt.setInt(5, news.getNewsId());
        return pstmt.executeUpdate();
    }


}
