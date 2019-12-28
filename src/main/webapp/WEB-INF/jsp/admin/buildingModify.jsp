<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
    function checkForm() {
        var buildingName = document.getElementById("buildingName").value;
        if (buildingName == null || buildingName === "") {
            document.getElementById("error").innerHTML = "名称不能为空！";
            return false;
        }
        return true;
    }
</script>
<div class="data_list">
    <div class="data_list_title">
        <c:choose>
            <c:when test="${building!=null }">
                修改场地
            </c:when>
            <c:otherwise>
                添加场地
            </c:otherwise>
        </c:choose>
    </div>
    <form action="building?action=save" method="post" onsubmit="return checkForm()">
        <div class="data_form">
            <input type="hidden" id="buildingId" name="buildingId" value="${building.id }"/>
            <table align="center">
                <tr>
                    <td><font color="red">*</font>名称：</td>
                    <td><input type="text" id="buildingName" name="buildingName" value="${building.name }"
                               style="margin-top:5px;height:30px;"/></td>
                </tr>
                <tr>
                    <td><font color="red">*</font>价格：</td>
                    <td><input type="text" id="buildingPrice" name="buildingPrice" value="${building.price }"
                               style="margin-top:5px;height:30px;"/></td>
                </tr>
                <tr>
                    <td>&nbsp;简介：</td>
                    <td><textarea id="buildingDescription" name="buildingDescription"
                                  rows="10">${building.description }</textarea></td>
                </tr>
            </table>
            <div align="center">
                <input type="submit" class="btn btn-primary" value="保存"/>
                &nbsp;<button class="btn btn-primary" type="button"
                              onclick="window.location='building?action=list'">返回
            </button>
            </div>
            <div align="center">
                <font id="error" color="red">${error }</font>
            </div>
        </div>
    </form>
</div>