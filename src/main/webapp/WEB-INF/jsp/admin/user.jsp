<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">

window.onload = function(){ 
	$("#DataTables_Table_0_wrapper .row-fluid").remove();
};
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
		width:0px;
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
		<form name="myForm" class="form-search" method="post" action="user?action=search" onsubmit="return checkSearch()" style="padding-bottom: 0px">
				<button class="btn btn-success" type="button" style="margin-right: 50px;" onclick="javascript:window.location='user?action=add'">添加</button>
				<span class="data_search">
					<font id="error" color="red">${error}</font>
					<select id="searchType" name="searchType" style="width: 80px;">
						<option value="name">姓名</option>
						<option value="id" >ID</option>
						<option value="sex" >性别</option>
					</select>
					&nbsp;<input id="id_search_user_text" name="search_user_text" type="text"  style="width:120px;height: 30px;" class="input-medium search-query" value="${search_user_text}">
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span>
		</form>
		<div>
			<table class="table table-striped table-bordered table-hover datatable">
				<thead>
					<tr>
					<!-- <th>编号</th> -->
					<th>ID</th>
					<th>用户名</th>
					<th>姓名</th>
					<th>性别</th>
					<th>电话</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach  varStatus="i" var="user" items="${user_list }">
					<tr>
						<%-- <td>${i.count+(page-1)*pageSize }</td> --%>
						<td>${user._id }</td>
						<td>${user._username }</td>
						<td>${user._name }</td>
						<td>${user._sex }</td>
						<td>${user._tel }</td>
						<td><button class="btn btn-mini btn-info" type="button" onclick="javascript:window.location='user?action=modify&id=${user._id }'">修改</button>&nbsp;
							<button class="btn btn-mini btn-danger" type="button" onclick="userDelete(${user._id })">删除</button></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<div align="center"><font color="red">${error }</font></div>
		<%-- <div class="pagination pagination-centered">
			<ul>
				${pageCode }
			</ul>
		</div> --%>
</div>