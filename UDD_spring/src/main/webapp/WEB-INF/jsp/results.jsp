<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page import="java.util.List" %>
<html>
<head>
<title>Search</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>
			<legend>Search results</legend>
			<c:if test="${data==null}">
				<br><span  style="color:red;font-size:20px">&nbsp; Result is empty.</span>
			
			</c:if>
			<c:if test="${suggest!=null}">
				<br>Did you mean:
				<br><table>
						<c:forEach items="${suggest}" var="rh">
							<tr>
								<td><a href="${rh.searchLink}"><c:out value="${rh.value}"/></a>
								</td>
							</tr>
						</c:forEach>
					</table>
				<br>
			</c:if>
			<br><span id="message" style="color:red;font-size:20px">&nbsp;</span>
			<table width="100%">
				<c:forEach items="${data}" var="res">
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td width="20%">Title</td><td>${res.title}</td></tr>
					<tr><td colspan="2">${res.highlight}</td></tr>
					<tr>
						<td colspan="2">
							<a href="${pageContext.request.contextPath}/finder/moreLikeThis?file=${res.fileName}" target="_blank">More Like This</a>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<sec:authorize access="hasRole('JOURNALIST') and isAuthenticated()">
	
	<div class="content">
		<div class="container">
		<a href="${pageContext.request.contextPath}/document/upload"><b>Upload</b></a>
		</div>
	</div>
	</sec:authorize>
</body>
</html>
