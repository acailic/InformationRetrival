<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<html>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>Read PDF</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>
			<legend>Read PDF</legend>
			<iframe src="/web/viewer.html?file=1467049107309_vercon.pdf" width="500px" height="400px" />
		</div>
	</div>
	<div class="content">
		<div class="container">
		<a href="${pageContext.request.contextPath}/document/upload"><b>Upload</b></a>
		</div>
	</div>
</body>
</html>
