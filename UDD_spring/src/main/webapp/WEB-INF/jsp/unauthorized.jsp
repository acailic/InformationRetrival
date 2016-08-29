<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<fmt:setBundle basename="messages.messages"/>

<html>
	<head>
		<title><fmt:message key="stranicaNijePronadjenaNaslov"/></title>
		<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<meta HTTP-EQUIV="Expires" CONTENT="-1">
	</head>
	<body>
		<div class="content">
		<div class="container">
	
		<jsp:include page="navbar.jsp"></jsp:include>
		
		<h2>You are not authorized to access this page!</h2>
		
		</div>
		</div>
	</body>
</html>	