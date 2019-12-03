<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
    window.onload = function(){
        $("#DataTables_Table_0_wrapper .row-fluid").remove();
    };
    function newsDelete(newsId) {
        if(confirm("您确定要删除这条新闻吗？")) {
            window.location="news?action=delete&newsId="+newsId;
        }
    }
</script>

<div class="data_list">
    <div class="data_list_title">
        新闻管理
    </div>
    <form name="myForm" class="form-search" method="post" action="news?action=search" style="padding-bottom: 0px">
        <span class="data_search">
					<font id="error" color="red">${error}</font>
					<select id="searchType" name="searchType" style="width: 80px;">
						<option value="name">标题</option>
						<option value="id" >ID</option>
					</select>
					&nbsp;<input id="id_search_user_text" name="search_user_text" type="text"  style="width:120px;height: 30px;" class="input-medium search-query" value="${search_user_text}">
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span>
    </form>
    <div>
        <table class="table table-striped table-bordered table-hover datatable">
            <thead>
            <button class="btn btn-mini btn-info" type="button" onclick="javascript:window.location='news?action=add'">发布新闻</button>
            </thead>
            <thead>
            <tr>
                <th>日期</th>
                <th>最近修改</th>
                <th>标题</th>
                <th>作者</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach  varStatus="i" var="news" items="${newsList }">
                <tr>
                    <td>${news.created }</td>
                    <td>${news.lastModified }</td>
                    <td>${news.title }</td>
                    <td>${news.author }</td>
                    <td>
                        <button class="btn btn-mini btn-info" type="button" onclick="javascript:window.location='news?action=preSave&newsId=${news.id }'">修改</button>&nbsp;
                        <button class="btn btn-mini btn-danger" type="button" onclick="newsDelete(${news.id })">删除</button></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div align="center"><font color="red">${error }</font></div>
</div>