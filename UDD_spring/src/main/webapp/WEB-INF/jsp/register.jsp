<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
 
<title>Registration</title>
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
			<c:if test="${adminAdd==true}">
				<c:set var="url"
					value="${pageContext.request.contextPath}/user/add" />
			</c:if>
			<c:if test="${adminAdd!=true}">
				<c:set var="url"
					value="${pageContext.request.contextPath}/user/register" />
			</c:if>

			<form class="form-horizontal" data-toggle="validator"
				action=${url} 
				method="post">
				<fieldset>
					<!-- Form Name -->
					<legend>Register</legend>
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Full
							name</label>
						<div class="col-md-4">
							<input id="textinput" name="ime" type="text" placeholder="Gabriel Batistuta"
								class="form-control input-md" data-maxlength="20"
								pattern="[A-z]*" required=""> <span
								class="help-block with-errors">Only letters.</span>
						</div>
					</div>
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Email</label>
						<div class="col-md-4">
							<input id="textinput" name="email" type="email"
								placeholder="batigol@gmail.com" class="form-control input-md"
								required=""> <span class="help-block with-errors"></span>
						</div>
					</div>
					<!-- Password input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="passwordinput">Password</label>
						<div class="col-md-4">
							<input id="passwordinput" name="lozinka" type="password"
								placeholder="xxxxx" class="form-control input-md" required="">
							<span class="help-block"></span>
						</div>
					</div>
					<!-- Button (Double) -->
					<div class="form-group">
						<label class="col-md-4 control-label" for="button1id"></label>
						<div class="col-md-8">
							<button id="button1id" name="button1id" type="submit"
								class="btn btn-warning">Submit</button>
							<button id="button2id" name="button2id" type="reset"
								class="btn btn-warning">Cancel</button>
						</div>
					</div>
				</fieldset>
			</form>

		</div>
	</div>
</body>
