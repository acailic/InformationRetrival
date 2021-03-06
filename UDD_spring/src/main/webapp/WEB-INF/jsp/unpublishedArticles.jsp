<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>Unpublished Articles</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">

</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>

			<legend>Unpublished Articles</legend>
			<table class="table table-bordered" style="background-color: white">
				<thead>
					<tr>
						<th>Author</th>
						<th>Title</th>
						<th>Abstract</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${unpublishedArticles}">
						<tr>
							<td>${item.author}</td>
							<td>${item.title}</td>
							<td>${item.abstractOfFile}</td>
							<td><a
								href="${pageContext.request.contextPath}/document/publish?id=${item.id}">Publish</a></td>
							<td><a
								href="${pageContext.request.contextPath}/document/notify?author=${item.author}?id=${item.id}?title=${item.title}">Notify journalist</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>