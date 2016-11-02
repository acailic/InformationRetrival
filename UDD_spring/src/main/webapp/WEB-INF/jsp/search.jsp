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
  <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
	<script> 
	$(function() { 
		$( "#datepicker" ).datepicker({ dateFormat : "dd-MM-yy"}); 
		}); 
	</script>
</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>
			<legend>Search</legend>
		
			<br><span id="message" style="color:red;font-size:20px">&nbsp;</span>
			<form action="${pageContext.request.contextPath}/finder/multisearch" method="post">
				<table>
					<tr>
						<th>Attribute</th>
						<th>Search value</th>
						<th>Search type</th>
						<th>Search condition</th>
					</tr>
					<tr>
						<td>Keywords</td>
						<td><input type="text" name="kw" class="form-control form-control-lg"/></td>
						<td><select class="selectpicker" name="kwst">
							<c:forEach items="${searchTypes}" var="st">
								<option value="${st}">${st}</option>
							</c:forEach>
							</select>
						</td>
						<td><select class="selectpicker" name="kwsc">
							<c:forEach items="${occures}" var="o">
								<option value="${o}">${o}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>Categories</td>
						<td><input type="text" name="ct" class="form-control form-control-lg"/></td>
						<td><select class="selectpicker"  name="ctst">
							<c:forEach items="${searchTypes}" var="st">
								<option value="${st}">${st}</option>
							</c:forEach>
							</select>
						</td>
						<td><select class="selectpicker" name="ctsc">
							<c:forEach items="${occures}" var="o">
								<option value="${o}">${o}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>Author</td>
						<td><input type="text" name="author" class="form-control form-control-lg"/></td>
						<td><select class="selectpicker" name="authorst">
							<c:forEach items="${searchTypes}" var="st">
								<option value="${st}">${st}</option>
							</c:forEach>
							</select>
						</td>
						<td><select  class="selectpicker" name="authorsc">
							<c:forEach items="${occures}" var="o">
								<option value="${o}">${o}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>Title</td>
						<td><input type="text" name="title" class="form-control form-control-lg"/></td>
						<td><select class="selectpicker" name="titlest">
							<c:forEach items="${searchTypes}" var="st">
								<option value="${st}">${st}</option>
							</c:forEach>
							</select>
						</td>
						<td><select class="selectpicker" name="titlesc">
							<c:forEach items="${occures}" var="o">
								<option value="${o}">${o}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>Abstract</td>
						<td><input type="text" name="abstractOfFile" class="form-control form-control-lg"/></td>
						<td><select class="selectpicker" name="abstractst">
							<c:forEach items="${searchTypes}" var="st">
								<option value="${st}">${st}</option>
							</c:forEach>
							</select>
						</td>
						<td><select class="selectpicker" name="abstractsc">
							<c:forEach items="${occures}" var="o">
								<option value="${o}">${o}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>Publish date</td>
						<td><input type="text" placeholder="publish date" name="publishDate" id="datepicker" 
						class="form-control form-control-lg"/></td>

						<td><select name="publishDatest">
							<c:forEach items="${searchTypes}" var="st">
								<option value="${st}">${st}</option>
							</c:forEach>
							</select>
						</td>
						<td><select class="selectpicker" name="publishDatesc">
							<c:forEach items="${occures}" var="o">
								<option value="${o}">${o}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>Text</td>
						<td><input type="text" name="text" class="form-control form-control-lg"/></td>
						<td><select  class="selectpicker" name="textst">
							<c:forEach items="${searchTypes}" var="st">
								<option value="${st}">${st}</option>
							</c:forEach>
							</select>
						</td>
						<td><select class="selectpicker" name="textsc">
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