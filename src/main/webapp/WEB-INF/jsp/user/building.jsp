<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="data_list">
	<div class="data_list_title">
		场地介绍
	</div>
	<div>
		<table class="table table-striped table-bordered table-hover datatable">
			<thead>
			<tr>
				<th width="15%">编号</th>
				<th>名称</th>
				<th>简介</th>
				<th>租金</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach varStatus="i" var="building" items="${buildingList }">
				<tr>
					<td>${building.id}</td>
					<td>${building.name }</td>
					<td>${building.description == null || building.description==""?"无":building.description }</td>
					<td>${building.price==null || building.price== ""?"无":building.price }</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<div align="center"><font color="red">${error }</font></div>
</div>
<script src="resources/media/js/moment.js"></script>
<script type="text/javascript">
	function checkForm() {
		var startDate = document.getElementById("startDate").value;
		var duration = document.getElementById("duration").value;
		if (startDate == null || startDate == "") {
			document.getElementById("error").innerHTML = "起始日期不能为空！";
			return false;
		}
		if (duration == null || duration == "") {
			document.getElementById("error").innerHTML = "天数不能为空！";
			return false;
		}
		if (isNaN(duration)) {
			document.getElementById("error").innerHTML = "天数必须为整数! ";
			return false;
		}
		if (!moment(startDate, 'YYYY-MM-DD', true).isValid()) {
			document.getElementById("error").innerHTML = "起始日期格式必须为 2019-01-01 ！";
			return false;
		}
		var now = moment(new Date());
		var date = moment(startDate, 'YYYY-MM-DD', true);
		if (now.diff(date) > 0) {
			document.getElementById("error").innerHTML = "请填写今天以后的日期 ！";
			return false;
		}
		return true;
	}

	$(document).ready(function () {
		$("ul li:eq(2)").addClass("active");
	});
</script>
<div class="data_list">
	<div class="data_list_title">
		我要预约
	</div>
	<form action="building?action=book" method="post" onsubmit="return checkForm()">
		<div class="data_form">
			<input type="hidden" id="recordId" name="recordId" value="${record.recordId }"/>
			<div>
				<table class="table table-striped table-bordered table-hover datatable">
					<thead>
					<tr>
						<th>场地</th>
						<th>起始日期</th>
						<th>天数</th>
						<th>操作</th>
					</tr>
					</thead>
					<tbody>
					<tr>
						<td>
							<select id="buildingId" name="buildingId" style="margin-top:5px;height:30px;">
								<c:forEach var="building" items="${buildingList }">
									<option value="${building.id }">${building.name }</option>
								</c:forEach>
							</select>
						</td>
						<td><input type="text" id="startDate" name="startDate" style="margin-top:5px;height:30px;"/>
						</td>
						<td><input type="text" id="duration" name="duration" style="margin-top:5px;height:30px;"/></td>
						<td><input type="submit" class="btn btn-primary" value="预约"/></td>
					</tr>
					</tbody>
				</table>
				<div align="center">
					<font id="error" color="red">${error }</font>
				</div>
			</div>
		</div>
	</form>
</div>