<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
function userDelete(id) {
	if(confirm("您确定要删除这个用户吗？")) {
		window.location="user?action=delete&id="+id;
	}
}

function checkSearch(){
	var keyword = document.getElementById("searchType").value;
	var argument = document.getElementById("id_search_user_text").value;
	if (keyword=="sex") {
		if (argument != "MALE" && argument != "FEMALE") {
			document.getElementById("error").innerHTML = "性别只能为MALE或FEMALE";
			return false;
		}
	}
	return "user?action=search";
}
</script>
<style type="text/css">
	.span6 {
		width: 0px;
		height: 0px;
		padding-top: 0px;
		padding-bottom: 0px;
		margin-top: 0px;
		margin-bottom: 0px;
	}

</style>
<div class="data_list">
	<div class="data_list_title">
		用户管理
	</div>
	<div>
		<table class="table table-striped table-bordered table-hover datatable">
			<thead>
			<tr>
				<th>ID</th>
				<th>用户名</th>
				<th>姓名</th>
				<th>性别</th>
				<th>电话</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach varStatus="i" var="user" items="${userList }">
				<tr>
					<td>${user.id }</td>
					<td>${user.username }</td>
					<td>${user.name }</td>
					<td>${user.sex }</td>
					<td>${user.tel }</td>
					<td>
						<button class="btn btn-mini btn-info" type="button"
								onclick="javascript:window.location='user?action=modify&id=${user.id }'">修改
						</button>&nbsp;
						<button class="btn btn-mini btn-danger" type="button" onclick="userDelete(${user.id })">删除
						</button>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<div align="center"><font color="red">${error }</font></div>
</div>