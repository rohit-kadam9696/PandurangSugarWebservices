<!DOCTYPE html>
<%@page import="com.twd.pandurangsugar.both.constant.WebConstant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<title><%=WebConstant.projectName%>  | Home</title>
<%@include file="css_link.jsp" %>
<%@include file="js_link.jsp" %>
<%@include file="table_link.jsp" %>
</head>
<body>
<section id="container">
<%@include file="header.jsp" %>
<!--header end-->
<%@include file="menu.jsp" %>
<!--main content start-->
	<section class="wrapper">
	<%HashMap<String,Object> dashboard=(HashMap<String,Object>)request.getAttribute("dashboard");
		if(dashboard==null){
			dashboard=new HashMap<String,Object>();
		}
		%>
		
				<div class="market-updates">
			<div class="col-md-4 market-update-gd">
				<div class="market-update-block clr-block-1">
					<div class="col-md-4 market-update-right">
						<i class="glyphicon glyphicon-user" style="font-size: 50px;color: white;"> </i>
					</div>
					 <div class="col-md-8 market-update-left">
					 <h4>Total&nbsp;Employee </h4>
					<h3><%=dashboard.containsKey("employee")?dashboard.get("employee"):0 %></h3>
					
				  </div>
				  <div class="clearfix"> </div>
				</div>
			</div>
			<div class="col-md-4 market-update-gd">
				<div class="market-update-block clr-block-2">
					<div class="col-md-4 market-update-right">
						<i class="glyphicon glyphicon-user" style="font-size: 50px;color: white;"> </i>
					</div>
					<div class="col-md-8 market-update-left">
					<h4>Total&nbsp;Customer</h4>
						<h3><%=dashboard.containsKey("customer")?dashboard.get("customer"):0 %></h3>
					</div>
				  <div class="clearfix"> </div>
				</div>
			</div>
			<div class="col-md-4 market-update-gd">
				<div class="market-update-block clr-block-3">
					<div class="col-md-4 market-update-right">
						<i class="fa fa-truck" style="font-size: 50px;color: white;"> </i>
					</div>
					<div class="col-md-8 market-update-left">
						<h4>Own / OutSource Vehicle</h4>
						<h3><%=dashboard.containsKey("ownvehicle")?dashboard.get("ownvehicle"):0 %> / <%=dashboard.containsKey("outSourceVehicle")?dashboard.get("outSourceVehicle"):0 %></h3>
						
					</div>
				  <div class="clearfix"> </div>
				</div>
			</div>
		<div class="clearfix"> </div>
		</div>	
			
			
		<div class="market-updates">
			<div class="col-md-4 market-update-gd">
				<div class="market-update-block clr-block-1">
					<div class="col-md-4 market-update-right">
						<i class=" 	glyphicon glyphicon-list-alt" style="font-size: 50px;color: white;"> </i>
					</div>
					 <div class="col-md-8 market-update-left">
					 <h4>Today&nbsp;LR </h4>
					<h3><%=dashboard.containsKey("todayLr")?dashboard.get("todayLr"):0 %></h3>
					
				  </div>
				  <div class="clearfix"> </div>
				</div>
			</div>
			<div class="col-md-4 market-update-gd">
				<div class="market-update-block clr-block-2">
					<div class="col-md-4 market-update-right">
						<i class="glyphicon glyphicon-book" style="font-size: 50px;color: white;"> </i>
					</div>
					<div class="col-md-8 market-update-left">
					<h4>Today&nbsp;Invoice</h4>
						<h3><%=dashboard.containsKey("todayInvoice")?dashboard.get("todayInvoice"):0 %></h3>
					</div>
				  <div class="clearfix"> </div>
				</div>
			</div>
			<div class="col-md-4 market-update-gd">
				<div class="market-update-block clr-block-3">
					<div class="col-md-4 market-update-right">
						<i class="glyphicon glyphicon-oil" style="font-size: 50px;color: white;"> </i>
					</div>
					<div class="col-md-8 market-update-left">
						<h4>Today Diesel(Ltr.)</h4>
						<h3><%=dashboard.containsKey("todayDiesel")?dashboard.get("todayDiesel"):0 %></h3>
						
					</div>
				  <div class="clearfix"> </div>
				</div>
			</div>
		<div class="clearfix"> </div>
		</div>	
		
		
		<div class="market-updates">
			<div class="col-lg-12">
	             <section class="panel">
	                         <div class="panel-body">
	                        		 
	                        </div>
	                    </section>       
	      </div>
	      
		<div class="clearfix"> </div>
		</div>
		
       
		 <!-- footer -->
		 <%@include file="footer.jsp" %>
  <!-- / footer -->
</section>

<!--main content end-->
</section>
<script type="text/javascript">
$(document).ready( function () {
	  var table = $('.example').DataTable( {
	    pageLength : 5,
	    lengthMenu: [[5, 10, 20, -1], [5, 10, 20,'All']]
	  } )
	} );
</script>
</body>
</html>
