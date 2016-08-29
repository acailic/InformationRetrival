<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>Izmjena korisnika</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">


</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>
			<form class="form-horizontal" action="${pageContext.request.contextPath}/user/edit"
				method="post" data-toggle="validator" accept-charset="UTF-8">
				<fieldset>

					<!-- Form Name -->
					<legend> Izmjena podataka </legend>
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Ime</label>
						<div class="col-md-4">
							<input id="textinput" name="name" type="text"
								value=${editUser.name } class="form-control input-md"
								data-maxlength="20" pattern="[A-z]*" required=""> <span
								class="help-block with-errors">Karakteri moraju biti
								slova.</span>

						</div>
					</div>
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Email</label>
						<div class="col-md-4">
							<input id="textinput" name="email" type="email"
								value=${editUser.email } placeholder="marko@gmail.com"
								class="form-control input-md" readonly> <span
								class="help-block with-errors"></span>
						</div>
					</div>
					 <input type="hidden" name="name" value=${editUser.name } >
					 <input type="hidden" name="passwordHash" value=${editUser.passwordHash } >
					 <input type="hidden" name="role" value=${editUser.role } >
					 <input type="hidden" name="id" value=${editUser.id } >
					
					<!-- Button (Double) -->
					<div class="form-group">
						<label class="col-md-4 control-label" for="button1id"></label>
						<div class="col-md-8">
							<button id="button1id" name="button1id" type="submit"
								class="btn btn-warning">Save</button>
							<button id="button2id" name="button2id" type="reset"
								class="btn btn-warning">Cancel</button>
						</div>
					</div>

				</fieldset>
			</form>

		</div>
	</div>
</body>
</html>