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
	<div class="container-fluid" controller="app">
		<div class="row" style="background-color: #159957; height: 48px;">
			<div class="col-md-2 col-lg-2 col-xs-2">
				<img alt="logo" src="assets/img/logo.png" />
			</div>
			<div class="col-md-10 col-lg-10 col-xs-10">
				<div class="col-md-3 col-lg-3 col-xs-3">
					<span style="font-size: 18px">fork:1</span>
				</div>
				<div class="col-md-3 col-lg-3 col-xs-3">
					<span style="font-size: 18px">stars:8</span>
				</div>
				<div class="col-md-6 col-lg-6 col-xs-6">
					github:<a href="#">点击我</a>
				</div>
			</div>
		</div>

		<div class="row menu" style="">
			<div class="col-md-2 col-lg-2 col-xs-2" style="">
				<form action="searchMenu">
					<div class="row">
						<!-- <i class="glyphicon glyphicon-search"></i> &nbsp;  -->
						<input class="form-control" name="{{name}}" placeholder="search"
							ng-model="model" ng-change="change({val: model})" />
					</div>
					<div class="row">
						<ul class="menu" style="list-style-type: none;">
							<li><i class="glyphicon glyphicon-home"></i> &nbsp; <a
								href="##">服务器管理 </li>
							<li><i class="glyphicon glyphicon-tasks"></i> &nbsp; <a
								href="##">客户端列表 </li>
							<li><i class="glyphicon glyphicon-certificate"></i> &nbsp; <a
								href="##">信息监控 </li>
							<li><i class="glyphicon glyphicon-bell"></i> &nbsp; <a
								href="##">日志管理 </li>
							<li><i class="glyphicon glyphicon-thumbs-up"></i> &nbsp; <a
								href="##">关于 </li>
						</ul>
					</div>
				</form>
			</div>
			<div class="col-md-10 col-lg-10 col-xs-10"
				style="background-color: gray;">
				<form action="searchByModule">
					<div class="row" style="">
						<input class="form-control" name="{{name}}" placeholder="search"
							ng-model="model" ng-change="change({val: model})" />
					</div>
					<div class="row" ng-view></div>
				</form>
			</div>
		</div>
	</div>
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
