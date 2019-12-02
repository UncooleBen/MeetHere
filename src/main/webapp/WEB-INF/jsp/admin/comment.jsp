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
    function commentFinalDelete(commentId) {
        if(confirm("您确定要删除这条留言吗？")) {
            window.location="comment?action=finalDelete&commentId="+commentId;
        }
    }
    function commentDelete(commentId) {
        if(confirm("您确定要删除这条留言吗？")) {
            window.location="comment?action=delete&commentId="+commentId;
        }
    }
    function commentApprove(commentId) {
        if(confirm("您确定要通过这条留言吗？")) {
            window.location="comment?action=approve&commentId="+commentId;
        }
    }
</script>

<div class="data_list">
	<div class="data_list_title">
		已审核留言
	</div>
	<form name="myForm1" class="form-search" method="post" action="comment?action=search" style="padding-bottom: 0px">
				<!--span class="data_search">
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
					</select>
					<option value="number" ${searchType eq "number"?'selected':'' }>客户</option>
					</select>
					&nbsp;<input id="s_userText" name="s_userText" type="text"  style="width:120px;height: 30px;" class="input-medium search-query" value="${s_userText }">
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span-->
	</form>
	<div>
		<table class="table table-striped table-bordered table-hover datatable">
			<thead>
			<tr>
				<th>日期</th>
				<th>客户</th>
				<th>留言内容</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach  varStatus="i" var="comment" items="${commentFinalList }">
				<tr>
					<td>${comment.date }</td>
					<td>${comment.userNumber }</td>
					<td>${comment.detail }</td>
					<td>
						<button class="btn btn-mini btn-danger" type="button" onclick="commentFinalDelete(${comment.commentId })">删除</button></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<div align="center"><font color="red">${error }</font></div>
	<div class="data_list_title">
		待审核留言
	</div>
	<form name="myForm" class="form-search" method="post" action="comment?action=search" style="padding-bottom: 0px">
		<!--span class="data_search">
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
					</select>
					<option value="number" ${searchType eq "number"?'selected':'' }>客户</option>
					</select>
					&nbsp;<input id="s_userText" name="s_userText" type="text"  style="width:120px;height: 30px;" class="input-medium search-query" value="${s_userText }">
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span-->
	</form>
	<div>
		<table class="table table-striped table-bordered table-hover datatable">
			<thead>
			<tr>
				<th>日期</th>
				<th>客户</th>
				<th>留言内容</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach  varStatus="i" var="comment" items="${commentList }">
				<tr>
					<td>${comment.date }</td>
					<td>${comment.userNumber }</td>
					<td>${comment.detail }</td>
					<td>
                        <button class="btn btn-mini btn-danger" type="button" onclick="commentApprove(${comment.commentId })">通过</button>
                        <button class="btn btn-mini btn-danger" type="button" onclick="commentDelete(${comment.commentId })">删除</button>
                    </td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<div align="center"><font color="red">${error }</font></div>
</div>