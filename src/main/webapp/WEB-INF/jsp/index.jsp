<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>

<html lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>MeetHere场地预约与管理系统</title>

    <link href="resources/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <script src="resources/bootstrap/js/jQuery.js"></script>
    <script src="resources/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript">
      function checkForm() {
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;
        if (username == null || username == "") {
          document.getElementById("error").innerHTML = "用户名不能为空";
          return false;
        }
        if (password == null || password == "") {
          document.getElementById("error").innerHTML = "密码不能为空";
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
    <form name="myForm" class="form-signin" action="login" method="post"
          onsubmit="return checkForm()">
        <h2 class="form-signin-heading"><font color="gray">MeetHere</font></h2>
        <input id="username" name="username" value="${user.username }" type="text"
               class="input-block-level"
               placeholder="账号">
        <input id="password" name="password" value="${user.password }" type="password"
               class="input-block-level"
               placeholder="密码">
        <font id="error" color="red">${error }</font>
        <div>
            <button class="btn btn-large btn-primary" type="submit">登录</button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button class="btn btn-large btn-primary" type="reset">重置</button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button class="btn btn-large btn-primary" type="button"
                    onclick="window.location.href='signup'">注册
            </button>
        </div>
        <p align="center" style="padding-top: 15px;">彭钧涛 李尚真 郭源杰</p>
    </form>
</div>
</body>
</html>