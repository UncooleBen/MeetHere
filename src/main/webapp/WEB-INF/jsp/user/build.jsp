<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
	$(document).ready(function(){
		$("ul li:eq(3)").addClass("active");
	});
</script>
<div class="data_list">
		<div class="data_list_title">
			场地介绍
		</div>
		<form name="myForm" class="form-search" method="post" action="building?action=search">
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
						<th>租金</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach  varStatus="i" var="building" items="${buildingList }">
					<tr>
						<td>${i.count+(page-1)*pageSize }</td>
						<td>${building.buildName }</td>
						<td>${building.detail==null||building.detail==""?"无":building.detail }</td>
						<td>${building.price==null||building.price==""?"无":building.price }</td>
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
<script type="text/javascript">
    function checkForm(){
        var buildName=document.getElementById("buildName").value;
        var roomName=document.getElementById("roomName").value;
        var date=document.getElementById("date").value;
        if(buildName==null||buildName==""){
            document.getElementById("error").innerHTML="楼不能为空！";
            return false;
        }
        if(roomName==null||roomName==""){
            document.getElementById("error").innerHTML="房间不能为空！";
            return false;
        }
        if(date==null){
            document.getElementById("error").innerHTML="日期不能为空！";
            return false;
        }
        return true;
    }

    $(document).ready(function(){
        $("ul li:eq(2)").addClass("active");
    });
</script>
<div class="data_list">
	<div class="data_list_title">
		我要预约
	</div>
	<form action="record?action=save" method="post" onsubmit="return checkForm()">
		<div class="data_form" >
			<input type="hidden" id="recordId" name="recordId" value="${record.recordId }"/>
			<div>
				<table class="table table-striped table-bordered table-hover datatable">
					<thead>
					<tr>
                        <th>起始日期</th>
                        <th>场地</th>
						<th>天数</th>
						<th>备注</th>
                        <th>操作</th>
					</tr>
					</thead>
					<tbody>
                    <tr>
                        <td><input type="text" id="date"  name="date" style="margin-top:5px;height:30px;" /></td>
                        <td>
                            <select id="buildId" name="buildId" style="margin-top:5px;height:30px;">
                                <c:forEach var="building" items="${buildingList }">
                                    <option value="${building.buildId }" ${buildToSelect==building.buildId?'selected':'' }>${building.buildName }</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td><input type="text" id="roomName" name="roomName" style="margin-top:5px;height:30px;" /></td>
                        <td><input type="text" id="detail" name="detail" style="margin-top:5px;height:30px;" /></td>
                        <td><input type="submit" class="btn btn-primary" value="预约"/></td>
                    </tr>
					</tbody>
				</table>
			<div align="center">
				<font id="error" color="red">${error }</font>
			</div>
		</div>
	</form>
</div>