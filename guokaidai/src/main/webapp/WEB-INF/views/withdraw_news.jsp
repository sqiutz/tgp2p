<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- 该页面为测试环讯注册页面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
</body>
	<h3>正在提交信息,请稍后......</h3>
	<form action="${map.url}" id="f456" method="post">
		<input type="hidden" name="pMerCode" value="${map.pMerCode}">
		<input type="hidden" name="p3DesXmlPara" value="${map.p3DesXmlPara}">
		<input type="hidden" name="pSign" value="${map.pSign}">
	</form>
	<script type="text/javascript">
 		document.getElementById("f456").submit();
    </script>  
</html>