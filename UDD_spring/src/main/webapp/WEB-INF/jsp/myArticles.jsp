<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>My Profile</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">

</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>
			<a href = "/web/viewer.html?file=1467049107309_vercon.pdf">View pdf</a>
			<legend>My Articles</legend>
			<table class="table table-bordered" style="background-color: white">
				<thead>
					<tr>
						<th>Title</th>
						<th>Abstract</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${results}">
						<tr>
							<td>${item.title}</td>
							<td>${item.abstractOfFile}</td>
							<c:if test="${item.publishTime=="null"}">
							<td><a
								href="${pageContext.request.contextPath}/document/edit?id=${item.id}">Update</a></td>
							</c:if>
							<td><a
								href="${pageContext.request.contextPath}/document/showPDF?file=${item.location}">Read</a></td>
						</tr>
					</c:forEach>
				</tbody>
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