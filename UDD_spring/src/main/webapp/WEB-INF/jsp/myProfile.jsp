<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>My Profile</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">
<script type="text/javascript">
			function checkStatus(){
				param = window.location.search;
				if(param.indexOf("?") != -1){
					message = param.split("?");
					if(message[1] == "success"){
						document.getElementById("message").innerHTML = "Password change successful";
					}else{
						document.getElementById("message").innerHTML = "Password change failed";
					}
				}
			}
</script>
</head>
<body>
	<div class="content">
		<div class="container">
		<c:set var="url"
					value="${pageContext.request.contextPath}/user/changePassword" />
			<jsp:include page="navbar.jsp"></jsp:include>
			<legend>My Profile</legend>
			<table class="table table-bordered" style="background-color: white">
				<thead>
					<tr>
						<th>E-Mail</th>
						<th>Name</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${currentUser.email}</td>
						<td>${currentUser.name}</td>
					</tr>
				</tbody>
			</table>
			<legend>Change password</legend>
			<form class="form-horizontal" data-toggle="validator" role="form"
				action=${url} method="post">
				<fieldset>

					<!-- Form Name -->
					<legend></legend>

					<!-- Password input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="passwordinput">Old password</label>
						<div class="col-md-4">
							<input id="passwordinput" name="oldPassword" type="password"
								placeholder="xxxxx" class="form-control input-md" required="">
							<span class="help-block"></span>
						</div>

					</div>
					
					<!-- Password input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="passwordinput">New password</label>
						<div class="col-md-4">
							<input id="passwordinput" name="newPassword" type="password"
								placeholder="xxxxx" class="form-control input-md" required="">
							<span class="help-block"></span>
						</div>

					</div>
					
					<!-- Password input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="passwordinput">Confirm new password</label>
						<div class="col-md-4">
							<input id="passwordinput" name="confirmNewPassword" type="password"
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
	<br><span id="message" style="color:red;font-size:20px">&nbsp;</span>
	<script type="text/javascript">
			checkStatus();
	</script>
</body>
</html>