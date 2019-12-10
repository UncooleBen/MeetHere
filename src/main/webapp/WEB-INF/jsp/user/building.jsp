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
<script type="text/javascript">
	function checkForm() {
		var buildName = document.getElementById("buildName").value;
		var roomName = document.getElementById("roomName").value;
		var date = document.getElementById("date").value;
		if (buildName == null || buildName == "") {
			document.getElementById("error").innerHTML = "楼不能为空！";
			return false;
		}
		if (roomName == null || roomName == "") {
			document.getElementById("error").innerHTML = "房间不能为空！";
			return false;
		}
		if (date == null) {
			document.getElementById("error").innerHTML = "日期不能为空！";
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