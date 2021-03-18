<!DOCTYPE html>
<html lang="en">
<head>
    <title>语法分析</title>
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-transform" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
	<script type="text/javascript" src="${request.contextPath}/static/js/jQuery-2.2.0.min.js"></script>
	<style>
	body *{
		padding:0px;
		margin: 0px;
	}
	.header{
		height:30px;
		width:100%;
		overflow:auto;
		border-style: solid;
  		border-width: 1px;
  		border-radius:5px;
  		margin-bottom:10px;
	}
	.header>div{
		padding:4px 4px;
	}
	.tag{
		font: 12px/1.5 Tahoma,Helvetica,Arial,'宋体',sans-serif;
		margin-left:20px;
		padding:2px 1px;
		border-style: solid;
  		border-width: 1px;
  		border-radius:2px;
	}
	.content{
		height:auto;
		padding:2px 1px;
		overflow:auto;
		border-style: solid;
  		border-width: 1px;
  		border-radius:5px;
	}

	.content *{
		padding:0px;
		margin: 0px;
	}
	.content .left{
		height:99%;
		width:49%;
		display:inline-block;
	}
	.content .left .textbox{
		width: 100%;
		height: 90%;
		display:block;
		background: #fffafa;
		font-family: 'FangSong', '微软雅黑', 'KaiTi_GB2312', Helvetica, 'Hiragino Sans GB', Arial, sans-serif;
		font-weight: bold;
		padding:1px 5px;
		font-size:13px;
		color:#228b22;
		border-style: solid;
  		border-width: 1px;
  		border-radius:5px;
		resize:none;
	}
	.content .left>div{
		padding: 2px 8px;
		display:block;
		margin-top:1%;
		float:right;
		margin-right:0px;
	}
	.content .right{
		height:99%;
		width: 49%;
		margin-right:1%;
		float:right;
		display:inline-block;
	}
	.content .right>div{
		width: 100%;
		height: 90%;
		display:block;
		padding:1px 5px;
		border-style: solid;
  		border-width: 1px;
  		border-radius:5px;
  		overflow: auto;
	}	
	.content button{
		padding: 0px 8px;
	}
	</style>
</head>
<body>
<div class="header">
	<div><strong>默认库:</strong>
	<span class="tag">规则一</span>
	<span class="tag">规则二</span>
	<span class="tag">规则三</span>
	<span class="tag">计算斐波那契数</span>
	<span class="tag">转换为日期格式</span>
	<span class="tag">现在</span>
	</div>
</div>
<div class="content">
	<div class="left">
		<textarea rows="" cols="" id="code" class="textbox"></textarea>
		<div>
		<button onclick="lexer()">词法分析</button>
		<!-- 不支持函数调用
		<button onclick="parse()">语法分析</button>
		<button onclick="eval()" style="left:20px;">执行</button>
		-->
		<button onclick="parse_f()">语法分析(EN)</button>
		<button onclick="eval_f()" style="left:20px;">执行(EN)</button>
		<button onclick="parse_ch()">语法分析(CH)</button>
		<button onclick="eval_ch()" style="left:20px;">执行(CH)</button>
		</div>
	</div>
	<div class="right">
		<div id="result" name="result">
		</div>
	</div>
</div>
<script>
var window_height=0;
var window_width=0;
//var webRoot = "${request.contextPath}/";

$(document).ready(function(){
	window_height 	= $(window).innerHeight() - 30;
	window_width 	= $(window).innerWidth()  - 19;
	$(".content").css({"height":(window_height-40),"width":window_width});
});

function lexer(){
	var code = $("#code").val();
	var data = {
		"code": code
	};
	$.ajax({
	    type: "post",
	    url: "${request.contextPath}/parser/lexer",
	    contentType: "application/json",
	    data: JSON.stringify(data),
	    dataType: "text",
	    success: function (data) {
			$("#result").html(data);
	    },
	    error: function (e) {
	    	$("#result").text(JSON.stringify(e.responseJSON));
	    }
	});	
}

function parse(){
	var code = $("#code").val();
	var data = {
		"code": code
	};
	$.ajax({
	    type: "post",
	    url: "${request.contextPath}/parser/parse",
	    contentType: "application/json",
	    data: JSON.stringify(data),
	    dataType: "text",
	    success: function (data) {
			$("#result").html(data);
	    },
	    error: function (e) {
	    	$("#result").text(JSON.stringify(e.responseJSON));
	    }
	});	
}

function eval(){
	var code = $("#code").val();
	var data = {
		"code": code
	};
	$.ajax({
	    type: "post",
	    url: "${request.contextPath}/parser/eval",
	    contentType: "application/json",
	    data: JSON.stringify(data),
	    dataType: "text",
	    success: function (data) {
			$("#result").html(data);
	    },
	    error: function (e) {
	    	$("#result").text(JSON.stringify(e.responseJSON));
	    }
	});	
}

function parse_f(){
	var code = $("#code").val();
	var data = {
		"code": code
	};
	$.ajax({
	    type: "post",
	    url: "${request.contextPath}/parser/parse_f",
	    contentType: "application/json",
	    data: JSON.stringify(data),
	    dataType: "text",
	    success: function (data) {
			$("#result").html(data);
	    },
	    error: function (e) {
	    	$("#result").text(JSON.stringify(e.responseJSON));
	    }
	});	
}

function eval_f(){
	var code = $("#code").val();
	var data = {
		"code": code
	};
	$.ajax({
	    type: "post",
	    url: "${request.contextPath}/parser/eval_f",
	    contentType: "application/json",
	    data: JSON.stringify(data),
	    dataType: "text",
	    success: function (data) {
			$("#result").html(data);
	    },
	    error: function (e) {
	    	$("#result").text(JSON.stringify(e.responseJSON));
	    }
	});	
}

function parse_ch(){
	var code = $("#code").val();
	var data = {
		"code": code
	};
	$.ajax({
	    type: "post",
	    url: "${request.contextPath}/parser/parse_ch",
	    contentType: "application/json",
	    data: JSON.stringify(data),
	    dataType: "text",
	    success: function (data) {
			$("#result").html(data);
	    },
	    error: function (e) {
	    	$("#result").text(JSON.stringify(e.responseJSON));
	    }
	});	
}

function eval_ch(){
	var code = $("#code").val();
	var data = {
		"code": code
	};
	$.ajax({
	    type: "post",
	    url: "${request.contextPath}/parser/eval_ch",
	    contentType: "application/json",
	    data: JSON.stringify(data),
	    dataType: "text",
	    success: function (data) {
			$("#result").html(data);
	    },
	    error: function (e) {
	    	$("#result").text(JSON.stringify(e.responseJSON));
	    }
	});	
}
</script>
</body>
</html>