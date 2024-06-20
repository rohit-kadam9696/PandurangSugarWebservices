<!doctype html>
<html class="no-js" lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title><%=request.getAttribute("errorcode") %> Page | Error</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  
</head>

<body>
   
   <section id="container" style="text-align: center;">
 <img alt="" src="images/error.png">
           
<h1>ERROR <span class="counter"> <%=request.getAttribute("errorcode") %></span></h1>
				<p>Message : <%=request.getAttribute("message") %></p>
				<p style="color: red">Info : <%=request.getAttribute("msg") %></p>
				<p>URL : <%=request.getAttribute("requestURl") %></p>
				<a href="dashboard">DashBoard</a>
				<a href="logout">LogOut</a>
		<%@include file="footer.jsp" %>
</section>

   <!--  <div class="error-pagewrap" style="background-image: url('img/logo/baground.jpg');width: 100%">
		<div class="error-page-int">
			<div class="content-error">
				
			</div>
			<div class="text-center login-footer">
				<p>Copyright © 2022 All rights reserved by <a href="http://3wdsoft.com">3WD SOFTWARE</a></p>
			</div>
		</div>   
    </div> -->
  
</body>

</html>