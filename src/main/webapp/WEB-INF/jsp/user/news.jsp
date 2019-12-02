<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
    $(document).ready(function(){
        $("ul li:eq(4)").addClass("active");
        $('.form_date').datetimepicker({
            language:  'en',
            weekStart: 1,
            todayBtn:  1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: 0
        });
        $('.datatable').dataTable( {
            "oLanguage": {
                "sUrl": "/DormManage/media/zh_CN.json"
            },
            "bLengthChange": false, //改变每页显示数据数量
            "bFilter": false, //过滤功能
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                { "asSorting": [ ] },
                { "asSorting": [ ] }
            ]
        });
        $("#DataTables_Table_0_wrapper .row-fluid").remove();
    });

    window.onload = function(){
        $("#DataTables_Table_0_wrapper .row-fluid").remove();
    };
    function newsDetail(newsID) {
        window.location="news?action=detail&newsId="+newsID;
    }
</script>

<!--div class="data_list">
    <div class="data_list_title">
        新闻
    </div>
    <form name="myForm" class="form-search" method="post" action="news?action=search" style="padding-bottom: 0px">
				<span class="data_search">
					<span class="controls input-append date form_date" style="margin-right: 10px" data-date="" data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd">
                    	<input id="startDate" name="startDate" style="width:120px;height: 30px;" placeholder="起始日期" type="text" value="${startDate }" readonly >
                    	<span class="add-on"><i class="icon-remove"></i></span>
						<span class="add-on"><i class="icon-th"></i></span>
               		</span>
					<span class="controls input-append date form_date" style="margin-right: 10px" data-date="" data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd">
                    	<input id="endDate" name="endDate" style="width:120px;height: 30px;" placeholder="终止日期" type="text" value="${endDate }" readonly>
                    	<span class="add-on"><i class="icon-remove"></i></span>
						<span class="add-on"><i class="icon-th"></i></span>
               		 </span>
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span>
    </form>
    <div>
        <table class="table table-striped table-bordered table-hover datatable">
            <thead>

            </thead>
            <thead>
            <tr>
                <th>日期</th>
                <th>标题</th>
                <th>作者</th>
                <th>内容</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach  varStatus="i" var="news" items="${newsList }">
                <tr>
                    <td>${news.date }</td>
                    <td>${news.title }</td>
                    <td>${news.author }</td>
                    <td>${news.detail }</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div align="center"><font color="red">${error }</font></div>
</div-->
<!--div class="data_box">
    <div class="lines">
        <text class="data_box_title">日期</text>
        <text class="data_box_title">标题</text>
        <text class="data_box_title">作者</text>
        <text class="data_box_title">内容</text>
    </div>
    <c:forEach  varStatus="i" var="news" items="${newsList }">
        <div class="lines">
            <text class="data_box_title">${news.date }</text>
            <text class="data_box_title">${news.title }</text>
            <text class="data_box_title">${news.author }</text>
            <text class="data_box_title">${news.detail }</text>
        </div>
    </c:forEach>
</div-->
<div class="data_list">
    <div class="data_list_title">
        新闻
    </div>
        <div class="data_box">
            <c:forEach  varStatus="i" var="news" items="${newsList }">
            <div class="news" >
                <div class="image">
                    <div class="cover"><img src="images/news.png"></div>
                </div>
                <div class="right">
                    <div class="data_box_title">
                        <p>${news.title }</p>
                    </div>
                    <div class="data_box_text">
                        <p>${news.author }</p>
                    </div>
                    <div class="data_box_text">
                        <p>${news.date }</p>
                    </div>
                    <div class="btn" onclick=newsDetail(${news.newsId })>查看</div>
                </div>
            </div>
            </c:forEach>
        </div>
</div>