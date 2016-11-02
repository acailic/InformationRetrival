<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<head>
		<link rel="shortcut icon" href="images/logo.png">
		<title>Upload</title>
		<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<meta HTTP-EQUIV="Expires" CONTENT="-1">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/bootstrap.min.css">
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
		<script type="text/javascript">
			function checkStatus(){
				param = window.location.search;
				if(param.indexOf("?") != -1){
					message = param.split("?");
					if(message[1] == "success"){
						document.getElementById("message").innerHTML = "File upload successful";
					}else{
						document.getElementById("message").innerHTML = "File upload failed";
					}
				}
			}
		</script>
	</head>
	<body>
		<div class="content">
			<div class="container">
				<c:set var="url"
					value="${pageContext.request.contextPath}/document/upload" />
			<jsp:include page="navbar.jsp"></jsp:include>
			<form class="form-horizontal" data-toggle="validator"
				action=${url} 
				method="post" accept-charset="UTF-8" enctype="multipart/form-data">
				<fieldset>
					<!-- Form Name -->
					<legend>Add article</legend>
					
					<!-- Password input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="fileinput">Document</label>
						<div class="col-md-4">
							<input id="uploadfile" name="uploadfile" type="file"
								class="form-control input-md" required="">
							<span class="help-block"></span>
						</div>
					</div>
					<!-- Button (Double) -->
					<div class="form-group">
						<label class="col-md-4 control-label" for="button1id"></label>
						<div class="col-md-8">
							<button id="button1id" name="button1id" type="submit"
								class="btn btn-warning">Confirm</button>
						</div>
					</div>
				</fieldset>
			</form>
			<br><span id="message" style="color:red;font-size:20px">&nbsp;</span>
			</div>
		</div>
		
		<script type="text/javascript">
			checkStatus();
		</script>
		<br>
	</body>