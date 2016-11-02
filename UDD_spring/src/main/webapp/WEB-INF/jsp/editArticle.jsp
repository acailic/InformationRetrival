<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>Edit Article</title>
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="-1">
<script src='https://cdn.tinymce.com/4/tinymce.min.js'></script>

 <script>
  tinymce.init({
    selector: '#mytextarea'
  });
  </script>
</head>
<body>
	<div class="content">
		<div class="container">
			<jsp:include page="navbar.jsp"></jsp:include>

			<legend>Edit Article</legend>
			<form method="post" action= "./EditorController">
    			<textarea id="mytextarea" name="content" >
    				${content}
    			</textarea>
    			<input type="submit" value="OK">
  			</form>
		</div>
	</div>
</body>
</html>