<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
    function checkForm() {
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;
        var rPassword = document.getElementById("rPassword").value;
        var name = document.getElementById("name").value;
        var sex = document.getElementById("sex").value;
        var tel = document.getElementById("tel").value;
        if (username === "" || password === "" || rPassword === "" || name === "" || sex === "" || tel === "") {
            document.getElementById("error").innerHTML = "信息填写不完整！";
            return false;
        } else if (password !== rPassword) {
            document.getElementById("error").innerHTML = "密码填写不一致！";
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
        <c:choose>
            <c:when test="${user!=null}">
                修改用户信息
            </c:when>
            <c:otherwise>
                添加用户
            </c:otherwise>
        </c:choose>
    </div>
    <form action="user?action=save" method="post" onsubmit="return checkForm()">
        <div class="data_form">
            <input type="hidden" id="id" name="id" value="${user.id }"/>
            <table align="center">
                <tr>
                    <td><font color="red">*</font>用户名：</td>
                    <td><input type="text" id="username" name="username" value="${user.username }"
                               style="margin-top:5px;height:30px;"/></td>
                </tr>
                <tr>
                    <td><font color="red">*</font>密码：</td>
                    <td><input type="password" id="password" name="password"
                               value="${user.password }"
                               style="margin-top:5px;height:30px;"/></td>
                </tr>
                <tr>
                    <td><font color="red">*</font>重复密码：</td>
                    <td><input type="password" id="rPassword" name="rPassword" value="${user.password }"
                               style="margin-top:5px;height:30px;"/></td>
                </tr>
                <tr>
                    <td><font color="red">*</font>姓名：</td>
                    <td><input type="text" id="name" name="name" value="${user.name }"
                               style="margin-top:5px;height:30px;"/></td>
                </tr>
                <tr>
                    <td><font color="red">*</font>性别：</td>
                    <td>
                        <select id="sex" name="sex" style="width: 90px;">
                            <option value="">请选择...</option>
                            <option value="MALE" ${user.sex eq "MALE" ? 'selected' : '' }>男</option>
                            <option value="FEMALE" ${user.sex eq "FEMALE" ? 'selected' : ''}>女</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td><font color="red">*</font>联系电话：</td>
                    <td><input type="text" id="tel" name="tel" value="${user.tel }"
                               style="margin-top:5px;height:30px;"/></td>
                </tr>
            </table>
            <div align="center">
                <input type="submit" class="btn btn-primary" value="保存"/>
                &nbsp;<button class="btn btn-primary" type="button"
                              onclick="window.location='user?action=list'">返回
            </button>
            </div>
            <div align="center">
                <font id="error" color="red">${error }</font>
            </div>
        </div>
    </form>
</div>