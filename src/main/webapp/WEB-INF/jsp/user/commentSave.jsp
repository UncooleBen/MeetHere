<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript">
	function checkForm(){
        var detail=document.getElementById("detail".value;
        var date=document.getElementById("date").value;
        if(date==null){
            document.getElementById("error").innerHTML="日期不能为空！";
            return false;
        }
        if(detail==null){
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
		<c:choose>
			<c:when test="${user.studentId!=null }">
				留言
			</c:when>
			<c:otherwise>
				留言
			</c:otherwise>
		</c:choose>
		</div>
		<form action="comment?action=save" method="post" onsubmit="return checkForm()">
			<div>
				<input type="hidden" id="commentId" name="commentId" value="${comment.commentId }"/>
                <textarea  id="detail" name="detail" class="Mytextarea"></textarea>
				<div align="left">
						<input type="submit" class="btn btn-primary" value="保存"/>
						&nbsp;<button class="btn btn-primary" type="button" onclick="javascript:window.location='comment'">返回</button>
					</div>
					<div align="left">
						<font id="error" color="red">${error }</font>
					</div>
			</div>
		</form>
</div>