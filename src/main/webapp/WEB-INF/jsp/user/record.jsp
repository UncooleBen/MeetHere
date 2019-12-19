<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
$(document).ready(function(){
	$("ul li:eq(1)").addClass("active");
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
		]
	});
	$("#DataTables_Table_0_wrapper .row-fluid").remove();
});

window.onload = function () {
	$("#DataTables_Table_0_wrapper .row-fluid").remove();
};

function recordDelete(recordId) {
	if (confirm("您确定要取消本次预约吗？")) {
		window.location = "record?action=delete&recordId=" + recordId;
	}
}

function convertDate(millisecond, id) {
	date = new Date(millisecond);
	document.getElementById(id).innerText = date.toDateString();
}

function convertTime(millisecond, id) {
	var day = millisecond / (86400) + 1;
	document.getElementById(id).innerText = day.toString() + "天";
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
					<td id="verifiedStartDate${i.index}">
						<script>convertDate(${record.startDate },
								"verifiedStartDate${i.index}")</script>
					</td>
					<td id="verifiedEndDate${i.index}">
						<script>convertDate(${record.endDate}, "verifiedEndDate${i.index}")</script>
					</td>
					<td>${record.userId }</td>
					<td>${record.buildingId==null?"无":record.buildingId }</td>
					<td id="verifiedInterval${i.index}">
						<script>convertTime(${record.time }, "verifiedInterval${i.index}")</script>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<div align="center"><font color="red">${error }</font></div>
	<div class="data_list_title">
		待审核记录
	</div>
	<%--	<form name="myForm2" class="form-search" method="post" action="record?action=search" style="padding-bottom: 0px">--%>
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
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span-->
	<%--	</form>--%>
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
			<c:forEach varStatus="i" var="record" items="${unverifiedRecordList }">
				<tr>
					<td id="unverifiedStartDate${i.index}">
						<script>convertDate(${record.startDate },
								"unverifiedStartDate${i.index}")</script>
					</td>
					<td id="unverifiedEndDate${i.index}">
						<script>convertDate(${record.endDate},
								"unverifiedEndDate${i.index}")</script>
					</td>
					<td>${record.userId }</td>
					<td>${record.buildingId==null?"无":record.buildingId }</td>
					<td id="unverifiedInterval${i.index}">
						<script>convertTime(${record.time },
								"unverifiedInterval${i.index}")</script>
					</td>
					<td>
						<button class="btn btn-mini btn-danger" type="button"
								onclick="recordDelete(${record.id })">取消
						</button>
					</td>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<div align="center"><font color="red">${error }</font></div>
</div>
