<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
    function checkForm() {
        var title = document.getElementById("title").value;
        var author = document.getElementById("author").value;
        var detail = document.getElementById("detail").value;
        if (detail == null || detail === "") {
            document.getElementById("error").innerHTML = "内容不能为空！";
            return false;
        }
        if (title == null || title === "") {
            document.getElementById("error").innerHTML = "标题不能为空！";
            return false;
        }
        if (author == null || author === "") {
            document.getElementById("error").innerHTML = "作者不能为空！";
            return false;
        }
        return true;
    }
</script>
<div class="data_list">
    <div class="data_list_title">
        <c:choose>
            <c:when test="${news.id!=null }">
                新闻修改
            </c:when>
            <c:otherwise>
                新闻发布
            </c:otherwise>
        </c:choose>
    </div>
    <form action="news?action=save" method="post" onsubmit="return checkForm()">
        <div class="data_form">
            <input type="hidden" id="newsId" name="newsId" value="${news.id }"/>
            <table align="center">
                <tr>
                    <td>&nbsp;标题：</td>
                    <td><input type="text" id="title" name="title" value="${news.title }"></td>
                </tr>
                <tr>
                    <td>&nbsp;作者：</td>
                    <td><input type="text" id="author" name="author" value="${news.author }"></td>
                </tr>
                <tr>
                    <td>&nbsp;正文：</td>
                    <td><textarea id="detail" name="detail" rows="10">${news.detail }</textarea></td>
                </tr>
            </table>
            <div align="center">
                <input type="submit" class="btn btn-primary" value="保存"/>
                &nbsp;<button class="btn btn-primary" type="button"
                              onclick="window.location='news?action=list'">返回
            </button>
            </div>
            <div align="center">
                <font id="error" color="red">${error }</font>
            </div>
        </div>
    </form>
</div>