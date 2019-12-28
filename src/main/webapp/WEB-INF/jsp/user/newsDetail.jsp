<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
         isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<script>
  function convertDate(millisecond, id) {
    date = new Date(millisecond);
    document.getElementById(id).innerText = date.toDateString();
  }
</script>
<div class="data_list">
    <div class="data_list_title">
        新闻查看
    </div>
    <div class="newsBox">
        <div class="newsTitle" id="title">${news.title}</div>
        <div class="newsDA" id="author">${news.author }</div>
        <div class="newsDA" id="createdTime">
            <script>convertDate(${news.created }, "createdTime")</script>
        </div>
        <div class="data_box_text" id="detail">${news.detail }</div>
    </div>
</div>
</html>
