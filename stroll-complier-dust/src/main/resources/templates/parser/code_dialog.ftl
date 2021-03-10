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
	.content{
		padding:4px 4px;
		overflow:auto;
		border-style: solid;
  		border-width: 1px;
  		border-radius:5px;
	}
	.content *{
		padding:0 0;
		margin: 0 0;
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
		font-family: "Helvetica", "Arial", "Verdana", "宋体";
		//font-weight: bold;
		padding:1px 5px;
		font-size:15px;
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
		padding: 2px 8px;
	}
	</style>
</head>
<body>
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
	window_height = $(window).innerHeight()-30;
	window_width = $(window).innerWidth()-30;
	$(".content").css({"height":window_height,"width":window_width});
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