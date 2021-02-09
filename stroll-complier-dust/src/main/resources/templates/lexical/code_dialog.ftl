<!DOCTYPE html>
<html lang="en">
<head>
    <title>词法分析</title>
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-transform" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="stylesheet" href="css/common.css" type="text/css">
	<script type="text/javascript" src="js/jQuery-2.2.0.min.js"></script>
</head>
<body>
<div>
</div>
<script>
$.ajax({
    type: "post",
    url: url + "/lexical/AdminConsole/GetConfigurationByConfigurationKey",
    contentType: "application/json",
    data: `{
		"confKey":"${ary[i]}"
	}`,
    dataType: "json",
    async: false,
    success: function (data) {
		ospan.textContent = data.result.configurationDto.confValue;
    },
    error: function (e) {
    }
});
</script>
</body>
</html>