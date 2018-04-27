<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/publicJs.jsp"%>
<style type="text/css">
table {
	display: none;
	width: 500px;
	text-align: center;
	border-spacing: 0;
	border-collapse: collapse;
}

table tr td, table tr th {
	border: 1px solid black;
	height: 30px;
}
</style>
</head>
<body>
	<div>
		<form id="uploadForm" action="${basepath }/poi/uploadExcel.json"
			method="post" enctype="multipart/form-data"
			onsubmit="return uploadExcel();">
			<h2>Excel汇总</h2>
			<input type="text" name="fileName" id="fileName">
			<p>
				上传文件： <input id="file" type="file" name="file"
					accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
					onChange="if(this.value)insertTitle(this.value);" />
			</p>
			<input type="submit" value="上传" />
		</form>
		<select id="select" name="village" style="width: 100px;"></select> <input
			type="button" id="export" value="导出" onclick="exportExcel();" />
	</div>
	<div>
		<button id="check" onclick="check();">检查</button>
	</div>
	<div>
		<table id="tab">

		</table>
	</div>
	<script>
	var checkstats = 0;
		$(function() {
			getVillages();
		});
		function check() {
			$
					.ajax({
						url : '${basepath}/poi/checkData.json',
						type : 'post',
						dataType : "json",
						success : function(res) {
							checkstats = 1;
							$("#tab").show();
							$("#tab").empty();
							var th = "<tr><th>姓名</th><th>面积</th><th>一卡通账号</th><th>村名</th><th>年份</th><th>操作</th></tr>";
							$("#tab").append(th);
							for (var i = 0; i < res.length; i++) {
								var tr = "<tr><td>" + res[i].name + "</td><td>"
										+ res[i].area + "</td><td>"
										+ res[i].accNum + "</td><td>"
										+ res[i].village + "</td><td>"
										+ res[i].year
										+ "</td><td><button onclick=del('"
										+ res[i].id
										+ "')>删除</button></td></tr>";
								$("#tab").append(tr);
							}
						},
						error : function(res) {
							$("#tab").hide();
							checkstats = 2;
							alert("没有错误！！！");
						}
					});
		}

		function insertTitle(path) {
			var test1 = path.lastIndexOf("/"); //对路径进行截取
			var test2 = path.lastIndexOf("\\"); //对路径进行截取
			var test = Math.max(test1, test2)
			if (test < 0) {
				$("#fileName").attr("value", path);
			} else {
				$("#fileName").attr("value", path.substring(test + 1)); //赋值文件名
			}
		}

		function uploadExcel() {
			var fileName = $("#fileName").val();
			var suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (suffix == "xls" || suffix == "xlsx") {
				return true;
			}
			alert("只能上传Excel文件！！！")
			return false;
		}

		function exportExcel() {
			var village = $("#select").val();
			if(checkstats == 0){
				alert("请先进行检查！！！");
				return;
			}
			if(checkstats == 1){
				alert("检查有错误，可以删除或修改后重新导入！！！");
				return;
			}
			if (village == 0) {
				alert("请选择导出项！！！");
				return;
			}
			$.ajax({
				url : '${basepath}/poi/exportExcel.json',
				type : 'post',
				data : {
					"village" : village
				},
				dataType : "json",
				success : function(res) {
					var path = "${basepath}/" + res.path;
					window.location.href = path;
				},
				error : function(res) {
					console.log(res);
					history.go(0);
				}
			});
		}

		function getVillages() {
			$("#select").empty();
			$.ajax({
				url : '${basepath}/poi/listvillages.json',
				type : 'post',
				dataType : "json",
				success : function(res) {
					$("#select").append("<option value='0'>请选择</option>");
					for (var i = 0; i < res.length; i++) {
						var option = "<option value='"+res[i]+"'>" + res[i]
								+ "</option>";
						$("#select").append(option);
					}
				},
			});
		}

		function del(id) {
			$.ajax({
				url : '${basepath}/poi/del.json',
				type : 'post',
				data : {
					"id" : id
				},
				dataType : "json",
				success : function(res) {
					alert("删除成功");
					check();
				},
			});
		}
	</script>
</body>
</html>
