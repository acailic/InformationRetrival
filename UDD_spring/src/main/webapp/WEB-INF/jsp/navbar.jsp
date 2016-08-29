<%@ page contentType="text/html;"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<%-- <link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/navbar.css"> --%>

<sec:authentication var="principal" property="principal" />

<div id="custom-bootstrap-menu"
	class="navbar navbar-default navbar-static-top" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			 <b id="kup"  >Information retrival project</b>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-menubuilder">
				<span class="sr-only">Toggle navigation</span>  
			</button>
			<a class="navbar-brand" ><img src="${pageContext.request.contextPath}/css/novine.png"></a>
		</div>

		<div class="collapse navbar-collapse navbar-menubuilder">
			<ul class="nav navbar-nav navbar-right">

				<li class="active"><a href="../"><b>Home</b></a></li>
				<sec:authorize access="hasRole('EDITOR') and isAuthenticated()">
				<li><a
					href="${pageContext.request.contextPath}/user/journalists"><b>All journalists</b></a></li>
					<li><a href="${pageContext.request.contextPath}/document/allArticles"><b>Browse articles</b></a></li>
					<li><a href="${pageContext.request.contextPath}/finder/simpleSearch"><b>Simple search</b></a></li>
					<li><a href="${pageContext.request.contextPath}/finder/search"><b>Criteria search</b></a></li>
				<li><a
					href="${pageContext.request.contextPath}/document/unpublishedArticles"><b>Unpublished articles</b></a></li>
				<li><a
					href="${pageContext.request.contextPath}/document/editorArticles"><b>Published articles</b></a></li>
				<li><a href="${pageContext.request.contextPath}/logout"><b>Logout
								</b></a></li>
				</sec:authorize>
				<sec:authorize access="!isAuthenticated()">
					<li><a href="${pageContext.request.contextPath}/user/register"><b>Register
								</b></a></li>
					<li><a href="${pageContext.request.contextPath}/login"><b>Login
								</b></a></li>
					<li><a href="${pageContext.request.contextPath}/document/allArticles"><b>Browse articles</b></a></li>
					<li><a href="${pageContext.request.contextPath}/finder/simpleSearch"><b>Simple search</b></a></li>
					<li><a href="${pageContext.request.contextPath}/finder/search"><b>Criteria search</b></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('JOURNALIST') and isAuthenticated()">
				<li><a href="${pageContext.request.contextPath}/document/allArticles"><b>Browse articles</b></a></li>
					<li><a href="${pageContext.request.contextPath}/finder/simpleSearch"><b>Simple search</b></a></li>
					<li><a href="${pageContext.request.contextPath}/finder/search"><b>Criteria search</b></a></li>
				<li><a href="${pageContext.request.contextPath}/user/myProfile"><b>My Profile</b></a></li>
					<li><a href="${pageContext.request.contextPath}/document/upload"><b>Upload</b></a></li>
					<li><a href="${pageContext.request.contextPath}/document/myArticles"><b>My Articles</b></a></li>
					<li><a href="${pageContext.request.contextPath}/logout"><b>Logout
								</b></a></li>
				</sec:authorize>
					

				
			</ul>
		</div>
	</div>
	 
</div>
</hr></hr></hr>