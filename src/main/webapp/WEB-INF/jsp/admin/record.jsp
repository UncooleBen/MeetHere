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

window.onload = function () {
	$("#DataTables_Table_0_wrapper .row-fluid").remove();
};

function recordDelete(recordId) {
	if (confirm("您确定要删除这条记录吗？")) {
		window.location = "record?action=delete&recordId=" + recordId;
	}
}

function recordApprove(recordId) {
	if (confirm("您确定审核通过这条记录吗？")) {
		window.location = "record?action=verify&recordId=" + recordId;
	}
}
</script>

<div class="data_list">
	<div class="data_list_title">
		已审核记录
	</div>
	<div>
		<table class="table table-striped table-bordered table-hover datatable">
			<thead>
			<tr>
				<th>起始日期</th>
				<th>结束日期</th>
				<th>客户</th>
				<th>场地</th>
				<th>使用时间</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach varStatus="i" var="record" items="${verifiedRecordList }">
				<tr>
					<td>${record.startDate }</td>
					<td>${record.endDate}</td>
					<td>${record.userId }</td>
					<td>${record.buildingId==null?"无":record.buildingId }</td>
					<td>${record.time }</td>
					<!-- see user/comment.jsp function convertDate(millisecond, id) for time conversion
						Usage at line 39.
					-->
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<div align="center"><font color="red">${error }</font></div>
	<div class="data_list_title">
		待审核记录
	</div>
	<div>
		<table class="table table-striped table-bordered table-hover datatable">
			<thead>
			<tr>
				<th>起始日期</th>
				<th>结束日期</th>
				<th>客户</th>
				<th>场地</th>
				<th>使用时间</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach varStatus="i" var="record" items="${unverifiedRecordList }">
				<tr>
					<td>${record.startDate }</td>
					<td>${record.endDate}</td>
					<td>${record.userId }</td>
					<td>${record.buildingId==null?"无":record.buildingId }</td>
					<td>${record.time }</td>
						<%--						<td>${record.detail }</td>--%>
					<td>
						<button class="btn btn-mini btn-danger" type="button"
								onclick="recordApprove(${record.id })">通过
						</button>
						<button class="btn btn-mini btn-danger" type="button"
								onclick="recordDelete(${record.id })">取消
						</button>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<div align="center"><font color="red">${error }</font></div>
</div>