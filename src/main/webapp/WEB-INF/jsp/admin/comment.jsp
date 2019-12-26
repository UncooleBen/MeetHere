<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
    function commentDelete(id) {
        if (confirm("您确定要删除这条留言吗？")) {
            window.location = "comment?action=delete&id=" + id;
        }
    }

    function commentApprove(id) {
        if (confirm("您确定要通过这条留言吗？")) {
            window.location = "comment?action=verify&id=" + id;
        }
    }

    function convertDate(millisecond, id) {
        date = new Date(millisecond);
        document.getElementById(id).innerText = date.toDateString();
    }
</script>

<div class="data_list">
    <div class="data_list_title" id="#id_title">
        已审核留言
    </div>
    <div>
        <table class="table table-striped table-bordered table-hover datatable">
            <thead>
            <tr>
                <th>日期</th>
                <th>客户</th>
                <th>留言内容</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach varStatus="commentListLoop" var="comment" items="${commentList }">
                <tr>
                    <td id="commentListLoop${commentListLoop.index}">
                        <script>convertDate(${comment.date }, "commentListLoop${commentListLoop.index}");</script>
                    </td>
                    <td>${comment.userId }</td>
                    <td>${comment.content }</td>
                    <td>
                        <button class="btn btn-mini btn-danger" type="button" onclick="commentDelete(${comment.id })">
                            删除
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div align="center"><font color="red">${error }</font></div>
    <div class="data_list_title">
        待审核留言
    </div>
    <div>
        <table class="table table-striped table-bordered table-hover datatable">
            <thead>
            <tr>
                <th>日期</th>
                <th>客户</th>
                <th>留言内容</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach varStatus="unverifiedCommentListLoop" var="comment" items="${unverifiedCommentList }">
                <tr>
                    <td id="unverifiedCommentListLoop${unverifiedCommentListLoop.index}">
                        <script>convertDate(${comment.date }, "unverifiedCommentListLoop${unverifiedCommentListLoop.index}");</script>
                    </td>
                    <td>${comment.userId }</td>
                    <td>${comment.content }</td>
                    <td>
                        <button class="btn btn-mini btn-danger" type="button" onclick="commentApprove(${comment.id })">
                            通过
                        </button>
                        <button class="btn btn-mini btn-danger" type="button" onclick="commentDelete(${comment.id })">
                            删除
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div align="center"><font color="red">${error }</font></div>
</div>