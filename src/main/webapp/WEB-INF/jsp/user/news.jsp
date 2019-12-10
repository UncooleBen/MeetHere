<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
         isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
  $(document).ready(function () {
    $("ul li:eq(4)").addClass("active");
    $('.form_date').datetimepicker({
      language: 'en',
      weekStart: 1,
      todayBtn: 1,
      autoclose: 1,
      todayHighlight: 1,
      startView: 2,
      minView: 2,
      forceParse: 0
    });
    $('.datatable').dataTable({
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
        {"asSorting": []},
        {"asSorting": []}
      ]
    });
    $("#DataTables_Table_0_wrapper .row-fluid").remove();
  });

  window.onload = function () {
    $("#DataTables_Table_0_wrapper .row-fluid").remove();
  };

  function newsDetail(newsID) {
    window.location = "news?action=detail&newsId=" + newsID;
  }

  function convertDate(millisecond, id) {
    date = new Date(millisecond);
    document.getElementById(id).innerText = date.toDateString();
  }
</script>

<div class="data_list">
    <div class="data_list_title">
        新闻
    </div>
    <div class="data_box">
        <c:forEach varStatus="i" var="news" items="${newsList }">
            <div class="news">
                <div class="image">
                    <div class="cover"><img src="resources/images/news.png"></div>
                </div>
                <div class="right">
                    <div class="data_box_title">
                        <p>${news.title }</p>
                    </div>
                    <div class="data_box_text">
                        <p>${news.author }</p>
                    </div>
                    <div class="data_box_text">
                        <p id="createdTime${i.index}">
                            <script>convertDate(${news.created }, "createdTime${i.index}")</script>
                        </p>
                    </div>
                    <div class="btn" onclick=newsDetail(${news.id })>查看</div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>