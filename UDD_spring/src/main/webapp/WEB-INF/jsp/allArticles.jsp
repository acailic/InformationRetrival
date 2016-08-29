<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>All Articles</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">

</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>

			<legend>All Articles</legend>
			<table class="table table-bordered" style="background-color: white">
				<thead>
					<tr>
						<th>Publish date</th>
						<th>Title</th>
						<th>Abstract</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${results}">
						<tr>
							<td>${item.publishTime}</td>
							<td>${item.title}</td>
							<td>${item.abstractOfFile}</td>
							<td>Read</td>
						</tr>
					</c:forEach>
				</tbody>

			</table>

		</div>
	</div>
</body>
</html>