<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
 
<title>Login</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">

</head>
<body>
	<div class="content">
		<div class="container">

			<jsp:include page="navbar.jsp"></jsp:include>
			<form class="form-horizontal" data-toggle="validator" role="form"
				action="/login" method="post">
				<fieldset>

					<!-- Form Name -->
					<legend></legend>

					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Email</label>
						<div class="col-md-4">
							<input id="textinput" name="email" type="text"
								placeholder="batigol@gmail.com" class="form-control input-md" required="">
							<span class="help-block with-errors"></span>
						</div>

					</div>

					<!-- Password input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="passwordinput">Password</label>
						<div class="col-md-4">
							<input id="passwordinput" name="password" type="password"
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
