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
progress {
	display: inline-block;
	width: 160px;
	height: 20px;
	border: 1px solid #0064B4;
	background-color: #e6e6e6;
	color: #66CD00; /*IE10*/
}
/*ie6-ie9*/
progress ie {
	display: block;
	height: 100%;
	background: #0064B4;
}

progress::-moz-progress-bar {
	background: #66CD00;
}

progress::-webkit-progress-bar {
	background: #e6e6e6;
}

progress::-webkit-progress-value {
	background: #66CD00;
}
</style>
</head>
<body>
	<input type="file" id="file" multiple="multiple"
		accept="image/gif, image/jpeg, image/jpg, image/png, image/bmp"
		onchange="uploadFile(this,1)">
	<img src="" id="img0" width="80px" height="80px">
	<progress id="progressBar" value="0" max="100">
		<ie style="width:20%;"></ie>
	</progress>
	<span id="percentage"></span>
	<script type="text/javascript">
		function uploadFile() {
			var filesize = document.getElementById("file").files.length;
			for(var i = 0; i < filesize; i++){
				var fileVal = document.getElementById("file").files[i];
				var fileController = "${basepath}/upload.json";
				var file = new FormData();
				file.append("file", fileVal);
				file.append("fileName", fileVal.name);
				var xhr = new XMLHttpRequest();
				xhr.open("post", fileController, true);
				xhr.onload = function(data) {
					console.log(data);
					var result = eval("(" + xhr.response + ")");
					console.log(result);
					$("#img0").attr("src", "${basepath}/" + result.path);
				};
				// 实现进度条功能  
				xhr.upload.addEventListener("progress", progressFunction, false);
				xhr.send(file);
			}
		}

		function progressFunction(evt) {
			var progressBar = document.getElementById("progressBar");
			var percentageDiv = document.getElementById("percentage");
			if (evt.lengthComputable) {
				progressBar.max = evt.total;
				progressBar.value = evt.loaded;
				percentageDiv.innerHTML = Math.round(evt.loaded / evt.total
						* 100)
						+ "%";
			}
		}
	</script>
</body>
</html>