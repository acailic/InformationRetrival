<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>Update Document Metadata</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>
			<c:set var="url"
					value="${pageContext.request.contextPath}/document/updateMetadata" />

			<form class="form-horizontal" data-toggle="validator"
				action=${url} 
				method="post">
				<fieldset>
					<!-- Form Name -->
					<legend>Update Document Metadata</legend>
					<!-- Text input-->
					<c:if test="${title==true}">
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Title</label>
						<div class="col-md-4">
							<input id="textinput" name="titleOfFile" type="text" placeholder="Title"
								class="form-control input-md" data-maxlength="20"
								 required=""> 
						</div>
					</div>
					</c:if>
					<!-- Text input-->
					<c:if test="${abstractText==true}">
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Abstract</label>
						<div class="col-md-4">
							<input id="textinput" name="abstractOfFile" type="text" placeholder="Abstract"
								class="form-control input-md" data-maxlength="150"
								 required=""> 
						</div>
					</div>
					</c:if>
					<!-- Text input-->
					<c:if test="${categories==true}">
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Categories</label>
						<div class="col-md-4">
							<input id="textinput" name="categoriesOfFile" type="text" placeholder="Category1 Category2 Category3"
								class="form-control input-md" data-maxlength="150"
								 required=""> 
						</div>
					</div>
					</c:if>
					<!-- Text input-->
					<c:if test="${tags==true}">
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Tags</label>
						<div class="col-md-4">
							<input id="textinput" name="tagsOfFile" type="text" placeholder="tag1 tag2 tag3"
								class="form-control input-md" data-maxlength="150"
								 required=""> 
						</div>
					</div>
					</c:if>
					<!-- Text input-->
					<c:if test="${keywords==true}">
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Keywords</label>
						<div class="col-md-4">
							<input id="textinput" name="keywordsOfFile" type="text" placeholder="kw1 kw2 kw3"
								class="form-control input-md" data-maxlength="150"
								 required=""> 
						</div>
					</div>
					</c:if>
					<!-- File input-->
					<input id="uploadFile" name="uploadFile" type="hidden" value=${uploadFilePath}
								class="form-control input-md" required="">
					<!-- Button (Double) -->
					<div class="form-group">
						<label class="col-md-4 control-label" for="button1id"></label>
						<div class="col-md-8">
							<button id="button1id" name="button1id" type="submit"
								class="btn btn-warning">Continue upload</button>
						</div>
					</div>
				</fieldset>
			</form>

		</div>
	</div>
</body>
