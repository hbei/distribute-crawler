<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>AQServer Admin Console</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css" />
<link rel="stylesheet"
	href="<%=basePath%>css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="<%=basePath%>css/matrix-style.css" />
<link rel="stylesheet" href="<%=basePath%>css/matrix-media.css" />
<link href="font-awesome/css/font-awesome.css" rel="stylesheet" />
<script src="<%=basePath%>js/excanvas.min.js"></script>
<script src="<%=basePath%>js/jquery.min.js"></script>
<script src="<%=basePath%>js/jquery.ui.custom.js"></script>
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/nicescroll/jquery.nicescroll.min.js"></script>
<script src="<%=basePath%>js/matrix.js"></script>

</head>

<body>
	<!--Header-part-->
	<div id="header">
		<h1>
			<a href="dashboard.html">Admin Console</a>
		</h1>
	</div>
	<!--close-Header-part-->

	<!--top-Header-menu-->
	<div id="user-nav" class="navbar navbar-inverse">
		<ul class="nav">
			<li class="dropdown" id="profile-messages"><a title="" href="#"
				data-toggle="dropdown" data-target="#profile-messages"
				class="dropdown-toggle"> <i class="icon icon-user"></i>&nbsp; <span
					class="text">欢迎你，admin</span>&nbsp; <b class="caret"></b>
			</a>
				<ul class="dropdown-menu">
					<li><a href="#"><i class="icon-user"></i> 个人资料</a></li>
					<li class="divider"></li>
					<li><a href="#"><i class="icon-check"></i> 我的任务</a></li>
					<li class="divider"></li>
					<li><a href="login.html"><i class="icon-key"></i> 退出系统</a></li>
				</ul></li>
			<li class="dropdown" id="menu-messages"><a href="#"
				data-toggle="dropdown" data-target="#menu-messages"
				class="dropdown-toggle"> <i class="icon icon-envelope"></i>&nbsp;
					<span class="text">我的消息</span>&nbsp; <span
					class="label label-important">4</span>&nbsp; <b class="caret"></b>
			</a>
				<ul class="dropdown-menu">
					<li><a class="sAdd" title="" href="#"><i class="icon-plus"></i>
							新消息</a></li>
					<li class="divider"></li>
					<li><a class="sInbox" title="" href="#"><i
							class="icon-envelope"></i> 收件箱</a></li>
					<li class="divider"></li>
					<li><a class="sOutbox" title="" href="#"><i
							class="icon-arrow-up"></i> 发件箱</a></li>
					<li class="divider"></li>
					<li><a class="sTrash" title="" href="#"><i
							class="icon-trash"></i> 回收站</a></li>
				</ul></li>
			<li class=""><a title="" href="#"><i class="icon icon-cog"></i>
					<span class="text">&nbsp;设置</span></a></li>
			<li class=""><a title="" href="login.html"><i
					class="icon icon-share-alt"></i> <span class="text">&nbsp;退出系统</span></a></li>
		</ul>
	</div>
	<!--close-top-Header-menu-->

	<!--start-top-serch-->
	<div id="search">
		<input type="text" placeholder="搜索..." />
		<button type="submit" class="tip-bottom" title="Search">
			<i class="icon-search icon-white"></i>
		</button>
	</div>
	<!--close-top-serch-->

	<!--sidebar-menu-->
	<div id="sidebar" style="OVERFLOW-Y: auto; OVERFLOW-X: hidden;">
		<ul>
			<li class="submenu active"><a class="menu_a"
				link="server/detailServer.htm"><i class="icon icon-home"></i> <span>控制面板</span></a>
			</li>
			<li class="submenu"><a href="#"> <i class="icon icon-table"></i>
					<span>权限管理</span> <span class="label label-important">3</span>
			</a>
				<ul>
					<li><a class="menu_a" link="grid.html"><i
							class="icon icon-caret-right"></i>权限管理</a></li>
					<li><a class="menu_a" link="tables.html"><i
							class="icon icon-caret-right"></i>景点列表</a></li>
					<li><a class="menu_a" link="form-common.html"><i
							class="icon icon-caret-right"></i>景点管理</a></li>
				</ul></li>
			<li class="submenu"><a href="#"> <i class="icon icon-th"></i>
					<span>联盟广告</span> <span class="label label-important">6</span>
			</a>
				<ul>
					<li><a class="menu_a"
						link="${pageContext.request.contextPath }/manage/crawler"><i
							class="icon icon-caret-right"></i>启动抓取</a></li>
					<li><a class="menu_a"
						link="${pageContext.request.contextPath }/manage/yiqifa"><i
							class="icon icon-caret-right"></i>亿起发单品列表</a></li>
				</ul></li>
			<li class="submenu"><a href="#"> <i class="icon icon-th"></i>
					<span>发布广告</span> <span class="label label-important">6</span>
			</a>
				<ul>
					<li><a class="menu_a" link="form-wizard.html"><i
							class="icon icon-caret-right"></i>注册用户</a></li>
					<li><a class="menu_a" link="table2.html"><i
							class="icon icon-caret-right"></i>用户列表</a></li>

				</ul></li>
			<li class="submenu"><a href="#"> <i
					class="icon icon-info-sign"></i> <span>统计</span> <span
					class="label label-important">4</span>
			</a>
				<ul>
					<li><a class="menu_a" link="charts.html"><i
							class="icon icon-caret-right"></i>统计信息一</a></li>
					<li><a class="menu_a" link="charts.html"><i
							class="icon icon-caret-right"></i>统计信息一</a></li>
					<li><a class="menu_a" link="charts.html"><i
							class="icon icon-caret-right"></i>统计信息一</a></li>
					<li><a class="menu_a" link="charts.html"><i
							class="icon icon-caret-right"></i>统计信息一</a></li>
				</ul></li>
		</ul>
	</div>
	<!--sidebar-menu-->

	<!--main-container-part-->
	<div id="content">
		<!--breadcrumbs-->
		<div id="content-header">
			<div id="breadcrumb">
				<a href="index.html" title="Go to Home" class="tip-bottom"><i
					class="icon-home"></i> Home</a>
			</div>
		</div>
		<!--End-breadcrumbs-->
		<iframe src="errorxi.html" id="iframe-main" frameborder='0'
			style="width: 100%;"></iframe>
	</div>
	<!--end-main-container-part-->

	<script type="text/javascript">
		//初始化相关元素高度
		function init() {
			$("body").height($(window).height() - 80);
			$("#iframe-main").height($(window).height() - 90);
			$("#sidebar").height($(window).height() - 50);
		}

		$(function() {
			init();
			$(window).resize(function() {
				init();
			});
		});

		function goPage(newURL) {
			if (newURL != "") {
				if (newURL == "-") {
					resetMenu();
				} else {
					document.location.href = newURL;
				}
			}
		}

		function resetMenu() {
			document.gomenu.selector.selectedIndex = 2;
		}
	</script>
</body>

</html>