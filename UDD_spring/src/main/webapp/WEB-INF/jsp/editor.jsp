<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>D Press</title>
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



	<jsp:include page="header.jsp"></jsp:include>

	<div id="main_container">
		<c:if test="${poruka != null}">
			<div class="top_bar">
				<span style="font-size: 14px; color: red"> ${poruka} </span>
			</div>
		</c:if>

		<div id="main_content">
			<jsp:include page="menu.jsp"></jsp:include>
			<!-- end of menu tab -->
			<br />
			<jsp:include page="leftContent.jsp"></jsp:include>

			<!-- admin home -->

			<form method="post" action= "./EditorController">
    			<textarea id="mytextarea" name="content" >
    				${content}
    			</textarea>
    			<input type="submit" value="OK">
  			</form>


			<!-- end of admin home -->

		

			<!-- end of center content -->
			<jsp:include page="rightContent.jsp"></jsp:include>

			<!-- end of right content -->
		</div>
		<!-- end of main content -->
		<jsp:include page="footer.jsp"></jsp:include>
	</div>
	<!-- end of main_container -->
<body>
</html>