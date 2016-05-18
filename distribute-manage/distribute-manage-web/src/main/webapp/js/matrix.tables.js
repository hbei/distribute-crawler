$(document).ready(function() {
	initTable();
});
function initTable() {
	$('.data-table')
			.dataTable(
					{
						"bDestroy" : true,
						"bJQueryUI" : true,
						"bServerSide" : true,
						"bFilter" : false,// 去掉搜索框
						"sAjaxDataProp" : "aData",
						"sAjaxSource" : "../product/yiqifa?now="
								+ new Date().getTime(),
						"sPaginationType" : "full_numbers",
						"sDom" : '<""l>t<"F"fp>',
						"oLanguage" : {
							"sProcessing" : "正在加载中......",
							"sLengthMenu" : "每页显示 _MENU_ 条记录",
							"sZeroRecords" : "正在加载中......",
							"sEmptyTable" : "表中无数据存在！",
							"sInfo" : "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
							"sInfoEmpty" : "显示0到0条记录",
							"sInfoFiltered" : "数据表中共为 _MAX_ 条记录",
							"sSearch" : "搜索",
							"oPaginate" : {
								"sFirst" : "首页",
								"sPrevious" : "上一页",
								"sNext" : "下一页",
								"sLast" : "末页"
							}
						},
						// <th>商品信息</th>
						// <th>图片</th>
						// <th>价格</th>
						// <th>折扣</th>
						// <th>佣金比例</th>
						// <th>佣金</th>
						// <th>推广时间</th>
						// <th>品牌</th>
						// <th>推广链接</th>
						// <th>详情图片</th>
						// <!-- <th>截图</th> -->
						// <th>发布</th>

						// private String prdtImg;// COMMENT '图片链接',
						// private String prdtName;// COMMENT '商品名称',
						// private String prdtPrice;// COMMENT '商品价格',
						// private String prdtDiscount;// COMMENT '商品折扣',
						// private String commiRate;// COMMENT '佣金比例',
						// private String commi;// COMMENT '佣金',
						// private String brandingStart;// COMMENT '推广开始时间',
						// private String brandingEnd;// COMMENT '推广结束时间',
						// private String prdtBrand;// COMMENT '品牌',
						// private String prdtLink;// COMMENT '商品链接',
						// private String md5Mark;// COMMENT '商品链接的md5',
						// private String adLink;// COMMENT '推广链接',
						"aoColumns" : [ {
							"mDataProp" : "prdtName",
						}, {
							"mDataProp" : "prdtImg",
						}, {
							"mDataProp" : "prdtPrice",
						}, {
							"mDataProp" : "prdtDiscount",
						}, {
							"mDataProp" : "commiRate",
						}, {
							"mDataProp" : "commi",
						}, {
							"mDataProp" : "brandingEnd",
						}, {
							"mDataProp" : "prdtBrand",
						}, {
							"mDataProp" : "prdtLink",
						}, {
							"mDataProp" : "detailImgs",
						}, {
							"mDataProp" : "prdtLink",
						} ],
						aoColumnDefs : [
								{
									"aTargets" : [ 0 ],
									"mRender" : function(data, type, full) {
										return "<a target='_blank' href='"
												+ full.prdtLink + "'>" + data;
									}
								},
								{
									"aTargets" : [ 1 ],
									"mRender" : function(data, type, full) {
										return "<img src='" + data
												+ "' width='96' height='96' >";
									}
								},
								{
									"aTargets" : [ 6 ],
									"mRender" : function(data, type, full) {
										return full.brandingStart + "~"
												+ full.brandingEnd;
									}
								},
								{
									"aTargets" : [ 9 ],
									"mRender" : function(data, type, full) {
										/*
										 * console.log("-----------");
										 * console.log(data);
										 */
										if (data == null) {
											return "<a href='#' onclick='downImgs(\""
													+ full.prdtLink
													+ "\")'>下载图片</a>";
										} else {
											var html = "";
											var imgs = data.split(",");

											for ( var i in imgs) {
												html += "<img width='30' src='http://60.31.177.185:8888/"
														+ imgs[i] + "'>";
											}
											return html;
										}
									}
								},
								{
									"aTargets" : [ 10 ],
									"mRender" : function(data, type, full) {
										var publish = full.publish;
										
										var option="";
										var content = "选择模块<select id='modules_"
												+ full.id
												+ "' autocomplete='off' >"
										if (typeof (publish) == "undefined"||publish==null
												|| publish.indexOf("1") == -1) {// 没有
											option+="<option  value ='1'>新奇特</option>"
										}
										if (typeof (publish) == "undefined"||publish==null
												|| publish.indexOf("2") == -1) {
											option+="<option   value ='2'>大热门</option>"
										}
										if (typeof (publish) == "undefined"||publish==null
												|| publish.indexOf("3") == -1) {
											option+="<option   value ='3'>轻奢侈</option>"
										}
										if (typeof (publish) == "undefined"||publish==null
												|| publish.indexOf("4") == -1) {
											option+="<option   value='4'>巨优惠</option>"
										}
										if (typeof (publish) == "undefined"||publish==null
												|| publish.indexOf("5") == -1) {
											option+="<option   value='5'>新新闻</option>"
										}

										if(option==""){
											return "";
										}
										content+=option;
										content+="</select><button onclick='publish(\""
												+ full.id + "\")'>发布</button>";

										return content;
									}
								} ]
					});

	$('input[type=checkbox],input[type=radio],input[type=file]').uniform();

	$('select').select2();

	$("span.icon input:checkbox, th input:checkbox").click(
			function() {
				var checkedStatus = this.checked;
				var checkbox = $(this).parents('.widget-box').find(
						'tr td:first-child input:checkbox');
				checkbox.each(function() {
					this.checked = checkedStatus;
					if (checkedStatus == this.checked) {
						$(this).closest('.checker > span').removeClass(
								'checked');
					}
					if (this.checked) {
						$(this).closest('.checker > span').addClass('checked');
					}
				});
			});

}
function publish(id) {

	var sid = "modules_" + id;
	var v = $("#" + sid + " option:selected").attr("value");
	console.log(id + "   sid:" + v + "  " + v);
	$.ajax({
		type : "POST",
		url : "/product/publish",
		dataType : "json",
		data : {
			id : id,
			modules : v
		},
		success : function() {
			// $("#imgs").html("<>");
		}
	});
}
function downImgs(prdtLink) {
	$.ajax({
		type : "POST",
		url : "../product/downImgs",
		dataType : "json",
		data : {
			prdtLink : prdtLink
		},
		success : function() {
			// $("#imgs").html("<>");
		}
	});
}