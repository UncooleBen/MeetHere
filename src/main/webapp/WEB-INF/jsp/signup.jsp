<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<%
    if (request.getAttribute("user") == null) {
        String username = null;
        String password = null;
        String userType = null;


        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        if (userType == null) {
            userType = "";
        }

    }
%>
<html lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MeetHere场地预约与管理系统登录</title>
    <link href="resources/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <script src="resources/bootstrap/js/jQuery.js"></script>
    <script src="resources/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript">
        function checkForm() {
            let username = document.getElementById("username").value;
            let password = document.getElementById("password").value;
            let tel = document.getElementById("tel").value;
            let name = document.getElementById("name").value;
            if (username == null || username === "") {
                document.getElementById("error").innerHTML = "用户名不能为空";
                return false;
            }
            if (username.length < 5 || username.length > 15) {
                document.getElementById("error").innerHTML = "用户名必须在5-15个字符之间";
                return false;
            }
            if (username.indexOf(' ') !== -1) {
                document.getElementById("error").innerHTML = "用户名不能含有空格";
                return false;
            }
            if (password == null || password === "") {
                document.getElementById("error").innerHTML = "密码不能为空";
                return false;
            }
            if (password.length < 10 || password.length > 15) {
                document.getElementById("error").innerHTML = "密码必须在10-15个字符之间";
                return false;
            }
            if (password.indexOf(' ') !== -1) {
                document.getElementById("error").innerHTML = "密码不能含有空格";
                return false;
            }
            if (password === username) {
                document.getElementById("error").innerHTML = "用户名和密码不能相同";
                return false;
            }
            if (name == null || name === "") {
                document.getElementById("error").innerHTML = "姓名不能为空";
                return false;
            }
            if (tel == null || tel === "") {
                document.getElementById("error").innerHTML = "电话不能为空";
                return false;
            }
            if (tel.length !== 11 || !tel.match(/1[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]/)) {
                document.getElementById("error").innerHTML = "电话必须为以1起始的11位数字";
                return false;
            }
            return true;
        }
    </script>

    <style type="text/css">
        body {
            padding-top: 200px;
            padding-bottom: 40px;
            background-image: url('resources/images/cd.jpg');
            background-position: center;
            background-repeat: no-repeat;
            background-attachment: fixed;
        }

        .radio {
            padding-top: 10px;
            padding-bottom: 10px;
        }

        .form-signin-heading {
            text-align: center;
        }

        .form-signin {
            max-width: 300px;
            padding: 19px 29px 0px;
            margin: 0 auto 20px;
            background-color: #fff;
            border: 1px solid #e5e5e5;
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
            -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
        }

        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }

        .form-signin input[type="text"],
        .form-signin input[type="password"] {
            font-size: 16px;
            height: auto;
            margin-bottom: 15px;
            padding: 7px 9px;
        }
    </style>

</head>
<body>
<div class="container">
    <form name="myForm" class="form-signin" action="signupSubmit" method="post" onsubmit="return checkForm()">
        <h2 class="form-signin-heading"><font color="gray">MeetHere场地预约与管理系统</font></h2>
        <input id="username" name="username" value="" type="text" class="input-block-level" placeholder="用户名">
        <input id="name" name="name" value="" type="text" class="input-block-level" placeholder="姓名">
        <input id="password" name="password" value="" type="password" class="input-block-level" placeholder="密码">
        <label class="radio inline">
            <input id="sex_male" type="radio" name="sex" value="MALE" checked/> 男
        </label>
        <label class="radio inline">
            <input id="sex_female" type="radio" name="sex" value="FEMALE"/> 女
        </label>
        <input id="tel" name="tel" value="" type="text" class="input-block-level" placeholder="电话">
        <font id="error" color="red">${error }</font><br>
        <div align="center">
            <button class="btn btn-large btn-primary" type="submit">注册</button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button class="btn btn-large btn-primary" type="reset">重置</button>
        </div>
        <p align="center" style="padding-top: 15px;"></p>
    </form>
</div>
</body>
</html>