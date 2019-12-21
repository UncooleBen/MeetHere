<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<html lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MeetHere场地预约与管理系统</title>
    <link href="resources/style/dorm.css" rel="stylesheet">
    <link href="resources/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="resources/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" rel="stylesheet"
          media="screen">
    <link rel="stylesheet" type="text/css"
          href="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/dataTables.bootstra.css">
    <script type="text/javascript" src="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/jquery.js"></script>
    <script type="text/javascript" src="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/jquery.dataTables.js"></script>
    <script type="text/javascript" src="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="resources/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"
            charset="UTF-8"></script>
    <script type="text/javascript"
            src="resources/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.fr.js"
            charset="UTF-8"></script>
    <script src="resources/bootstrap/js/bootstrap.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $("#DataTables_Table_0_wrapper .row-fluid").remove();
        });
    </script>
    <style type="text/css">
        .bs-docs-sidenav {
            background-color: #fff;
            border-radius: 6px;
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.067);
            padding: 0;
            width: 228px;
        }

        .bs-docs-sidenav > li > a {
            border: 1px solid #e5e5e5;
            display: block;
            padding: 8px 14px;
            margin: 0 0 -1px;
        }

        .bs-docs-sidenav > li:first-child > a {
            border-radius: 6px 6px 0 0;
        }

        .bs-docs-sidenav > li:last-child > a {
            border-radius: 0 0 6px 6px;
        }

        .bs-docs-sidenav > .active > a {
            border: 0 none;
            box-shadow: 1px 0 0 rgba(0, 0, 0, 0.1) inset, -1px 0 0 rgba(0, 0, 0, 0.1) inset;
            padding: 9px 15px;
            position: relative;
            text-shadow: 0 1px 0 rgba(0, 0, 0, 0.15);
            z-index: 2;
        }

        .bs-docs-sidenav .icon-chevron-right {
            float: right;
            margin-right: -6px;
            margin-top: 2px;
            opacity: 0.25;
        }

        .bs-docs-sidenav > li > a:hover {
            background-color: #f5f5f5;
        }

        .bs-docs-sidenav a:hover .icon-chevron-right {
            opacity: 0.5;
        }

        .bs-docs-sidenav .active .icon-chevron-right, .bs-docs-sidenav .active a:hover .icon-chevron-right {
            background-image: url("../img/glyphicons-halflings-white.png");
            opacity: 1;
        }
    </style>

</head>
<body>
<div class="container-fluid" style="padding-right: 0px;padding-left: 0px;">
    <div region="north" style="height: 100px;background-image: url('resources/images/bg.jpg')">
        <div align="left" style="width: 80%;height:100px ;float: left;padding-top: 40px;padding-left: 30px;"><font
                color="white" size="6">MeetHere场地预约与管理系统</font></div>
        <div style="padding-top: 70px;padding-right: 20px;">当前用户：&nbsp;<font color="red">${currentUser.username }</font>
        </div>
    </div>
</div>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span2 bs-docs-sidebar">
            <ul class="nav nav-list bs-docs-sidenav">
                <li><a href="blank"><i class="icon-chevron-right"></i>首页</a></li>
                <li><a href="news?action=list"><i class="icon-chevron-right"></i>新闻</a></li>
                <li><a href="comment?action=list"><i class="icon-chevron-right"></i>留言板</a></li>
                <li><a href="building?action=list"><i class="icon-chevron-right"></i>场地预约</a></li>
                <li><a href="record?action=list"><i class="icon-chevron-right"></i>预约记录</a></li>
                <li><a href="password?action=change"><i class="icon-chevron-right"></i>修改密码</a></li>
                <li><a href="index"><i class="icon-chevron-right"></i>退出系统</a></li>
            </ul>
        </div>
        <div class="span10">
            <jsp:include page="${ mainPage==null ? 'user/blank.jsp' : mainPage }"></jsp:include>
        </div>
    </div>
</div>
</body>
</html>