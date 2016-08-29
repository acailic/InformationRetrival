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
 </head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>
			<hr>
			<legend>Search</legend>
		
			<br><span id="message" style="color:red;font-size:20px">&nbsp;</span>
			<form action="${pageContext.request.contextPath}/finder/singlesearch" method="post">
				<table>
					<tr>
						<th>Search value</th>
						<th>Search type</th>
						<th>Search condition</th>
					</tr>
					<tr>
						<td><input type="text" name="text"/></td>
						<td><select   name="textst">
							<c:forEach items="${searchTypes}" var="st">
								<option value="${st}">${st}</option>
							</c:forEach>
							</select>
						</td>
						<td><select    name="textsc">
							<c:forEach items="${occures}" var="o">
								<option value="${o}">${o}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr><td colspan="4" align="center"><input type="submit" value="Search"/></td></tr>
				</table>
			</form>
		</div>
	</div><hr>
	<sec:authorize access="hasRole('JOURNALIST') and isAuthenticated()">
				
	<div class="content">
		<div class="container">
		<a href="${pageContext.request.contextPath}/document/upload"><b>Upload</b></a>
		</div>
	</div>
	</sec:authorize>
</body>
</html>