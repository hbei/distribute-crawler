<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html>
<head>
		<title>亿起发单品列表</title>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		 
		<script src="<%=basePath %>js/jquery.min.js"></script>
		<script src="<%=basePath %>js/jquery.ui.custom.js"></script>
		<script src="<%=basePath %>js/bootstrap.min.js"></script>
		<script src="<%=basePath %>js/jquery.uniform.js"></script>
		<script src="<%=basePath %>js/select2.min.js"></script>
		<script src="<%=basePath %>js/jquery.dataTables.min.js"></script>
		<script src="<%=basePath %>js/matrix.js"></script>
		<script src="<%=basePath %>js/matrix.tables.js"></script>
		
		<link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css" />
		<link rel="stylesheet" href="<%=basePath %>css/bootstrap-responsive.min.css" />
		<link rel="stylesheet" href="<%=basePath %>css/uniform.css" />
		<link rel="stylesheet" href="<%=basePath %>css/select2.css" />
		<link rel="stylesheet" href="<%=basePath %>css/matrix-style2.css" />
		<link rel="stylesheet" href="<%=basePath %>css/matrix-media.css" />
		<link href="font-awesome/css/font-awesome.css" rel="stylesheet" />
		<link href='http://fonts.useso.com/css?family=Open+Sans:400,700,800' rel='stylesheet' type='text/css'>
	</head>
	<body>
		<div class="widget-box">
          <div class="widget-title"> <span class="icon"><i class="icon-th"></i></span>
            <h5>亿起发单品列表</h5>
          </div>
          <div class="widget-content nopadding">
            <table class="table table-bordered data-table">
              <thead>
                <tr>
                  <th>商品信息</th>
                  <th>图片</th>
                  <th>价格</th>
                  <th>折扣</th>
                  <th>佣金比例 </th>
                  <th>佣金 </th>
                  <th>推广时间 </th>
                  <th>品牌 </th>
                  <th>推广链接 </th>
                  <th>详情图片</th>
                <!--   <th>截图</th> -->
                  <th>发布</th>
                </tr>
              </thead>
              <script>
              
              	$(function(){
              		getData();
              	});
              	function getData(){
              		 $.ajax({
					   type: "POST",
					   url: "<%=basePath %>product/yiqifa",
					   dataType: "json",
					   data: {},
					   success: function(data){
						   setTable(data);
					   }
					});
              	}
              	function setTable(data){
					var $prdt_list = $("#prdt_list");
					$prdt_list.html("");
					for(var i in data){
						var row = data[i];
						var html = "";
						html+="<tr>";
						html+="<th><a target='_blank' href='"+row.prdtLink+"'>"+row.prdtName+"</th>";
						html+="<th><img src='"+row.prdtImg+"' width='96' height='96' ></th>";
						html+="<th>"+row.prdtPrice+"</th>";
						html+="<th>"+row.prdtDiscount+"</th>";
						html+="<th>"+row.commiRate+"</th>";
						html+="<th>"+row.commi+"</th>";
						html+="<th>"+row.brandingStart+"~"+row.brandingEnd+"</th>";
						html+="<th>"+row.prdtBrand+"</th>";
						html+="<th>"+row.prdtLink+"</th>";
						html+="<th><a href='#' onclick='downImgs(\""+row.prdtLink+"\")'>下载图片</a></th>";
						/* html+="<th><a href='#' onclick=''>网页截图</a></th>"; */
						html+="<th>"+
						"选择模块<select id='modules_"+row.id+"' autocomplete='off' >"+
						  "<option  value ='1'>新奇特</option>"+
						  "<option   value ='2'>大热门</option>"+
						  "<option   value ='3'>轻奢侈</option>"+
						  "<option   value='4'>巨优惠</option>"+
						  "<option   value='5'>新新闻</option>"+
						"</select><button onclick='publish("+row.id+")'>发布</button>"+
						"</th>";
						html+="</tr>";
						
						$(html).appendTo($prdt_list);
					}
				}
              	function downImgs(prdtLink){
              		 $.ajax({
  					   type: "POST",
  					   url: "<%=basePath %>product/downImgs",
  					   dataType: "json",
  					   data: {prdtLink:prdtLink},
  					   success: function(){
  						  // $("#imgs").html("<>");
  					   }
  					});	
              	}
              	function publish(id){
              		var sid= "modules_"+id;
              		var v = $("#"+sid+" option:selected").attr("value");
             		 $.ajax({
    					   type: "POST",
    					   url: "<%=basePath %>product/downImgs",
    					   dataType: "json",
    					   data: {id:id,modules:v},
    					   success: function(){
    						  // $("#imgs").html("<>");
    					   }
    					});	
              	}
              </script>
              <tbody id="prdt_list">
              </tbody>
            </table>
            
          </div>
          							
        </div>
	</body>
</html>
