<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>Article Update</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">


</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>
			<form class="form-horizontal" action="${pageContext.request.contextPath}/document/update"
				method="post" data-toggle="validator" accept-charset="UTF-8">
				<fieldset>

					<!-- Form Name -->
					<legend> Article Update </legend>
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Title</label>
						<div class="col-md-4">
							<input id="textinput" name="title" type="text"
								value=${editArticle.title } class="form-control input-md"
								data-maxlength="20" pattern="[A-z]*" required=""> <span
								class="help-block with-errors">Karakteri moraju biti
								slova.</span>

						</div>
					</div>
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Abstract</label>
						<div class="col-md-4">
							<input id="textinput" name="abstractOfArticle" type="text"
								value=${editArticle.abstractOfFile } 
								class="form-control input-md" > <span
								class="help-block with-errors"></span>
						</div>
					</div>
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Categories</label>
						<div class="col-md-4">
							<input id="textinput" name="categories" type="text"
								value=${editArticle.categories } 
								class="form-control input-md" > <span
								class="help-block with-errors"></span>
						</div>
					</div>
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Tags</label>
						<div class="col-md-4">
							<input id="textinput" name="tags" type="text"
								value=${editArticle.tags } 
								class="form-control input-md" > <span
								class="help-block with-errors"></span>
						</div>
					</div>
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="textinput">Keywords</label>
						<div class="col-md-4">
							<input id="textinput" name="keywords" type="text"
								value=${editArticle.keywords } 
								class="form-control input-md" > <span
								class="help-block with-errors"></span>
						</div>
					</div>
					<input type="hidden" name="id" value=${editArticle.id } >
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