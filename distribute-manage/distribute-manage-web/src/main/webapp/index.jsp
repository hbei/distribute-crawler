<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh" ng-app="admin">
<head>
<meta charset="utf-8">
<base href="<%=basePath %>">
<title>Admin Console | Starter</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css"
	rel="stylesheet">
<link href="css/main.css" rel="stylesheet">
</head>
<body>
	<%
			response.sendRedirect("login.htm");
		%>
</body>
<script src="//cdn.bootcss.com/jquery/2.0.1/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/angular.js/1.3.18/angular.min.js"></script>
<script
	src="//cdn.bootcss.com/angular-ui-bootstrap/1.3.2/ui-bootstrap-tpls.min.js"></script>
<script
	src="//cdn.bootcss.com/angular-ui-bootstrap/1.3.2/ui-bootstrap.min.js"></script>
<script src="js/aq.js"></script>
</html>
