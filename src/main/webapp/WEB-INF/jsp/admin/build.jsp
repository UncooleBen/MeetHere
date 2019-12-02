<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
	function buildDelete(buildId) {
		if(confirm("您确定要删除这个楼吗？")) {
			window.location="build?action=delete&buildId="+buildId;
		}
	}
	$(document).ready(function(){
		$("ul li:eq(3)").addClass("active");
	});
</script>
<div class="data_list">
		<div class="data_list_title">
			场地管理
		</div>
		<form name="myForm" class="form-search" method="post" action="build?action=search">
				<button class="btn btn-success" type="button" style="margin-right: 50px;" onclick="javascript:window.location='build?action=preSave'">添加</button>
				<span class="data_search">
					名称:&nbsp;&nbsp;<input id="s_buildName" name="s_buildName" type="text"  style="width:120px;height: 30px;" class="input-medium search-query" value="${s_buildName }">
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span>
		</form>
		<div>
			<table class="table table-striped table-bordered table-hover datatable">
				<thead>
					<tr>
						<th width="15%">编号</th>
						<th>名称</th>
						<th>简介</th>
						<th>价格</th>
						<th width="20%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach  varStatus="i" var="build" items="${buildList }">
					<tr>
						<td>${i.count+(page-1)*pageSize }</td>
						<td>${build.buildName }</td>
						<td>${build.detail==null||build.detail==""?"无":build.detail }</td>
						<td>${build.price==null||build.price==""?"无":build.price }</td>
						<td>
							<button class="btn btn-mini btn-info" type="button" onclick="javascript:window.location='build?action=preSave&buildId=${build.buildId }'">修改</button>&nbsp;
							<button class="btn btn-mini btn-danger" type="button" onclick="buildDelete(${build.buildId})">删除</button></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div align="center"><font color="red">${error }</font></div>
		<div class="pagination pagination-centered">
			<ul>
				${pageCode }
			</ul>
		</div>
</div>