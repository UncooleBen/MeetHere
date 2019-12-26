<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
    function buildDelete(buildingId) {
        if (confirm("您确定要删除这个楼吗？")) {
            window.location = "building?action=delete&id=" + buildingId;
        }
    }
</script>
<div class="data_list">
    <div class="data_list_title" id="#id_title">
        场地管理
    </div>
    <div>
        <table class="table table-striped table-bordered table-hover datatable">
            <thead>
            <button class="btn btn-mini btn-info" type="button" id="#id_add_button"
                    onclick="window.location='building?action=add'">添加场地
            </button>
            <tr>
                <th width="15%">编号</th>
                <th>名称</th>
                <th>简介</th>
                <th>价格</th>
                <th width="20%">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach varStatus="i" var="building" items="${buildingList }">
                <tr>
                    <td>${i.count+(page-1)*pageSize }</td>
                    <td>${building.name }</td>
                    <td>${building.description==null || building.description=="" ? "无":building.description }</td>
                    <td>${building.price==null || building.price=="" ? "无":building.price }</td>
                    <td>
                        <button class="btn btn-mini btn-info" type="button" id="#id_modify_button_${building.id}"
                                onclick="window.location='building?action=modify&id=${building.id }'">
                            修改
                        </button>&nbsp;
                        <button class="btn btn-mini btn-danger" type="button" id="#id_delete_button_${building.id}"
                                onclick="buildDelete(${building.id})">删除
                        </button>
                    </td>
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