<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<link rel="stylesheet" href="css/bootstrap.min.css">
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-1.11.1.min.js"></script>

<html>
<head>
<link rel="shortcut icon" href="images/logo.png">
<title>E-Articles</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
  
</head>


<body  >
<jsp:include page="navbar.jsp"></jsp:include>
	<div class="container" >
		 
		
		<div id="location">
			<h1 class="customheader text-center">Office Location</h1>
			<div class="text-center">
			<div>Information Retrival</div>
				<div>21000 Novi Sad</div> 
				<div>Laze Teleckog</div>
				<div>Serbia</div>
			</div>
			<div class="col-lg-1 col-offset-6 centered">
		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
		<div style="overflow:hidden;height:500px;width:900px;"><div id="gmap_canvas" style="height:500px;width:900px;"><style>#gmap_canvas img{max-width:none!important;background:none!important}</style><a class="google-map-code" href="http://www.themecircle.net" id="get-map-data">themecircle</a></div></div><script type="text/javascript"> function init_map(){var myOptions = {zoom:15,center:new google.maps.LatLng(45.2566078,19.845694399999957),mapTypeId: google.maps.MapTypeId.ROADMAP};map = new google.maps.Map(document.getElementById("gmap_canvas"), myOptions);marker = new google.maps.Marker({map: map,position: new google.maps.LatLng(45.2566078, 19.845694399999957)});infowindow = new google.maps.InfoWindow({content:"<b>Information Retrival</b><br/>Laze Teleckog<br/> Novi Sad" });google.maps.event.addListener(marker, "click", function(){infowindow.open(map,marker);});infowindow.open(map,marker);}google.maps.event.addDomListener(window, 'load', init_map);</script> </div>
	  </div>
	  </div>
	  
	  
	 
	<footer class="footer">
      <div class="container">
        <p> Copyright @Aleksandar Ilic.</p>
      </div>
    </footer>
	</div>
	
 	
</body>
</html>