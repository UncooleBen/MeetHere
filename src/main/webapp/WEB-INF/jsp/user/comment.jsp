<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
    function commentDelete(commentId) {
        if (confirm("您确定要删除这条留言吗？")) {
            window.location = "comment?action=delete&commentId=" + commentId;
        }
    }

    function convertDate(millisecond, id) {
        date = new Date(millisecond);
        document.getElementById(id).innerText = date.toDateString();
    }
</script>

<div class="data_list">
    <div class="data_list_title" id="#id_title">
        留言板
    </div>
    <button class="btn btn-mini btn-info" type="button" onclick="window.location='comment?action=add'">我要留言
    </button>&nbsp;
    <div class="data_box">
        <c:forEach varStatus="commentListLoop" var="comment" items="${commentList }">
            <div class="news">
                <div class="image">
                    <img src="images/default.png">
                </div>
                <div class="right">
                    <div class="data_box_title">
                        <p>${comment.userId }的留言</p>
                    </div>
                    <div class="data_box_text">
                        <p>${comment.content }</p>
                    </div>
                    <div class="data_box_text">
                        <p id="commentListLoop${commentListLoop.index}">
                            <script>convertDate(${comment.date}, "commentListLoop${commentListLoop.index}");</script>
                        </p>
                    </div>

                </div>
            </div>
        </c:forEach>
    </div>
</div>