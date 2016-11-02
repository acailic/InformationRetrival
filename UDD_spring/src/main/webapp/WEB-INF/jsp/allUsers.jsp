<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head> 
<title>All Journalist</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">

</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>

			<legend>All Journalist</legend>
			<a id="button2id" name="button2id"
				href="${pageContext.request.contextPath}/user/register"
				class="btn btn-primary btn-md">Add journalist</a>
			<hr>
			<table class="table table-bordered" style="background-color: white">
				<thead>
					<tr>
						<th>Email</th>
						<th>Ime</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="journalist" items="${allJournalists}">
						<tr>
							<td>${journalist.email}</td>
							<td>${journalist.name}</td>
							<td><a
								href="${pageContext.request.contextPath}/user/edit/${journalist.id}"> Modify</a></td>
						</tr>
					</c:forEach>
				</tbody>

			</table>

		</div>
	</div>
</body>
</html>