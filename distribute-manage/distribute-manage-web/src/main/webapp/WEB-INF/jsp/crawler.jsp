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
		<title>Matrix Admin</title>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		
		<script src="<%=basePath %>js/jquery.min.js"></script>
		
		<link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css" />
		<link rel="stylesheet" href="<%=basePath %>css/bootstrap-responsive.min.css" />
		<link rel="stylesheet" href="<%=basePath %>css/matrix-style2.css" />
		<link rel="stylesheet" href="<%=basePath %>css/matrix-media.css" />
		<link href="<%=basePath %>font-awesome/css/font-awesome.css" rel="stylesheet" />
		<link href='http://fonts.useso.com/css?family=Open+Sans:400,700,800' rel='stylesheet' type='text/css'>
	</head>

	<body>

		<div id="content">
			<div id="content-header">
				<h1>爬虫管理</h1>
			</div>
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span12">
						<div class="widget-box">
							<div class="widget-title"> <span class="icon"> <i class="icon-info-sign"></i> </span>
								<h5>启动爬虫</h5>
							</div>
							<div class="widget-content">
								<div class="error_ex">
									<p><button onclick="startYiqifaCrawler()">亿起发</button></p>
									
									<div class="error_ex">
										<table>
										<tr>
											<td>用户名</td>
											<td><input id="username" name="username" type="text"/></td>
										</tr>
											<tr>
												<td>密码</td>
												<td><input id="password" name="password" type="password"/></td>
											</tr>
											<tr>
												<td>验证码</td>
												<td><input id="checkCode" name="checkCode" type="checkCode"/></td>
												<td><a href="javascript:freshCheckCode();"><img id="checkcode_img" src="<%=basePath %>crawler/checkCode"/></a></td>
											</tr>
										</table>
										
										<button onclick="startYiqifaCrawler()">启动</button>
									</div>
									
									<script type="text/javascript">
										function startYiqifaCrawler(){
											alert("启动亿起发爬虫");
											//显示验证码
											//输入用户名、密码
											
											var username = $("#username").val();
											var password = $("#password").val();
											var checkCode = $("#checkCode").val();
											
											console.log("username:"+username);
											console.log("password:"+password);
											console.log("checkCode:"+checkCode);
											
											 $.ajax({
											   type: "POST",
											   url: "<%=basePath %>crawler/login",
											   dataType: "json",
											   data: {username:username,password:password,checkCode:checkCode},
											   success: function(data){
												   getExecHis();
												   //alert("success");
											   }
											 });
										}
										function freshCheckCode(){
											$("#checkcode_img").attr("src","<%=basePath %>crawler/checkCode?t="+new Date().getTime());
										}
									</script>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="container-fluid">
						<div class="widget-box">
							<div class="widget-title"> <span class="icon"> <i class="icon-th"></i> </span>
								<h5>运行记录</h5>
								<!--<span class="label label-info">Featured</span>--></div>
							<div class="widget-content ">
								<table class="table table-bordered table-striped with-check">
									<thead>
										<tr>
											<th>#</th>
											<th>登录状态</th>
											<th>登录时间</th>
											<th>运行状态</th>
											<th>抓取开始时间</th>
											<th>抓取结束时间</th>
										</tr>
									</thead>
									<script>
										$(function(){
											getExecHis();
										})
										//获取执行历史
										function getExecHis(){
											$.ajax({
												type: "POST",
												url: "<%=basePath %>crawler/history",
												dataType: "json",
												data: {},
												success: function(data){
													console.log(data);
													setTable(data);
												}
											});
										}
										function setTable(histories){
											var $exec_his = $("#exec_his");
											$exec_his.html("");
											for(var i in histories){
												var row = histories[i];
												var html = "";
												
												html+="<tr>";
												
												html+="<th>"+i+"</th>";//登录状态
												var loginStatus =row.loginStatus; 
												if(loginStatus!=null){
													if(loginStatus=="1"){
														loginStatus="成功";
													}else if(loginStatus=="2"){
														loginStatus="失败";
													}
												}else{
													loginStatus="";
												}
												html+="<th>"+loginStatus+"</th>";//登录状态
												
												var loginTime =row.loginTime; 
												if(loginTime!=null){
													var date = new Date(loginTime);
													loginTime=date;
												}else{
													loginTime="";
												}
												
												html+="<th>"+loginTime+"</th>";//登录时间
												
												var crawlStatus =row.crawlStatus; 
												if(crawlStatus!=null){
													if(crawlStatus=="0"){
														crawlStatus="未运行";
													}else if(crawlStatus=="1"){
														crawlStatus="<font style='color:red'>正在运行</font>";
													}else if(crawlStatus=="2"){
														crawlStatus="已结束：成功";
													}else if(crawlStatus=="3"){
														crawlStatus="已结束：失败";
													}
												}else{
													crawlStatus="";
												}
												html+="<th>"+crawlStatus+"</th>";//抓取状态
												
												var crawlStartTime =row.crawlStartTime; 
												if(crawlStartTime!=null){
													var date = new Date(crawlStartTime);
													crawlStartTime=date;
												}else{
													crawlStartTime="";
												}
												html+="<th>"+ crawlStartTime+"</th>";//抓取开始时间
												
												var crawlEndTime =row.crawlEndTime; 
												if(crawlEndTime!=null ){
													var date = new Date(crawlEndTime);
													crawlEndTime=date;
												}else{
													crawlEndTime="";
												}
												html+="<th>"+crawlEndTime+"</th>";//抓取结束时间
												html+="</tr>";
												
												$(html).appendTo($exec_his);
											}
										}
										
									</script>
									<tbody id="exec_his"> </tbody>
								</table>
							</div>
						</div>
					</div>
		
		</div>

		
<!-- 		<script src="js/jquery.ui.custom.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/maruti.html"></script> -->
	</body>

</html>