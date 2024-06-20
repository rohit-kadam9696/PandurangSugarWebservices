<%@page import="com.twd.pandurangsugar.web.bean.Permission"%>
<%@page import="com.twd.pandurangsugar.web.bean.Department"%>
<%@page import="com.twd.pandurangsugar.both.constant.Constant"%>
<%@page import="org.json.JSONArray"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html><html>
<head>
<title><%=WebConstant.projectName%>  | System User</title>
<%@include file="css_link.jsp" %>
<%@include file="js_link.jsp" %>
<%@include file="datepicker.jsp" %> 
<script type="text/javascript" src="js/angular/systemuser.js"> </script>
 <style type="text/css">
         .inner{
         margin-left: 30px !important;
         }
         .subinner{
         margin-left: 30px !important;
         }
         .modal-dialog {
    width: 80% !important;
}
      </style>
       
</head>
<body>
<%--  --%>
<section id="container">
<%@include file="header.jsp" %>
<!--header end-->

<%@include file="menu.jsp" %>
<!--sidebar end-->
<!--main content start-->

	<section class="wrapper">
		<div class="form-w3layouts">
            <!-- page start-->
            <div class="row">
                <div class="col-lg-12">
                    <section class="panel">
                   
                      <%@include file="message.jsp" %>
                        <div class="panel-body" >
                           <form class="form-horizontal"  ng-app="myapp" ng-controller='systemuserdtl' action="saveColor" method="post" id="userdetails" name="userdetails">
                            <div class="col-lg-12">
                          <header class="panel-heading">
						 System User Details
					</header>
					</div>
					 <div class="col-lg-12" style="margin-top: 1em" ng-init="loadData()">
					    <div class="form-group <%=txtclass%>">
						   			    <label class="col-lg-2 control-label  req_lbl"> User Code</label>
	                        			<div class="col-lg-4"> 
	                         			 		 <input type="text"   name="nuserName" id="nuserName" class="form-control" ng-model="nuserName"  required="required" ng-blur="getInfoById()">
	                                     		  <span class="error" ng-show="userdetails.nuserName.$error.required">User Code Required!</span>
	                                     </div>
	                                     	<label class="col-lg-2 control-label  req_lbl"> User Name</label>
	                         			<div class="col-lg-4"> 
	                         			 		 <input type="text"   name="vuserName" id="vuserName" class="form-control" ng-model="vuserName" required="required" ng-change="validateuserragister('vuserName',' user name is already register with .','checkUserNameExitOrNot')">
	                                     		  <span class="error" ng-show="userdetails.vuserName.$error.required">User Name Required!</span>
	                                     </div>
                             </div>
					 		<div class="form-group <%=txtclass%>">
							  		<label class="col-lg-2 control-label  req_lbl">Full Name</label>
                        			<div class="col-lg-4"> 
                         			 		 <input type="text"   name="vfullName" id="vfullName" class="form-control" ng-model="vfullName"  required="required">
                                     		  <span class="error" ng-show="userdetails.vfullName.$error.required">Full Name Required!</span>
                                     </div>
                                     	<label class="col-lg-2 control-label  req_lbl"> Full Name Local</label>
                         			<div class="col-lg-4"> 
                         			 		 <input type="text"   name="vfullNameLocal" id="vfullNameLocal" class="form-control" ng-model="vfullNameLocal" required="required">
                                     		  <span class="error" ng-show="userdetails.vfullNameLocal.$error.required">Full Name Local Required!</span>
                                     </div>
							</div>
						 
                             <div class="form-group <%=txtclass%>">
						   			    <label class="col-lg-2 control-label  req_lbl"> Department</label>
	                        			<div class="col-lg-4"> 
	                        			 		 <select name="ndeptId" id="ndeptId" class="form-control selectpicker" ng-model="ndeptId"  required="required" data-live-search="true">
	                                     		  <option value="" disabled>Select your option</option>
	                                     		  <option ng-repeat="option in deptList" value="{{option.nsubdeptId}}">{{option.vsubdeptNameLocal}}</option>
	                                     		  </select>
	                                     </div>
	                                     	<label class="col-lg-2 control-label  req_lbl"> User Role</label>
	                         			<div class="col-lg-4"> 
	                        					 <select name="nuserRoleId" id="nuserRoleId" class="form-control" ng-model="nuserRoleId"  required="required" ng-change="getSugarTypeAndLocationList()">
	                                     		 		<option value="" disabled>Select your option</option>
	                                     		 		<option ng-repeat="option in roleList" value="{{option.id}}">{{option.name}}</option>
	                                     		  </select>
	                                     </div>
                             </div>
                             <div class="form-group <%=txtclass%>">
						   			    <label class="col-lg-2 control-label  req_lbl"> Type</label>
	                        			<div class="col-lg-4"> 
	                        					<select name="nsugTypeId" id="nsugTypeId" class="form-control" ng-model="nsugTypeId"  required="required">
	                                     				<option value="" disabled>Select your option</option>
                                          				<option ng-repeat="option in sugTypeList" value="{{option.id}}">{{option.name}}</option>
	                                     		</select>
	                                     </div>
	                                     	<label class="col-lg-2 control-label">Location</label>
	                         			<div class="col-lg-4"> 
	                        					<select name="nlocationId" id="nlocationId" class="form-control" ng-model="nlocationId">
	                        							<option value="" disabled>Select your option</option>
                                          				<option ng-repeat="option in locationList" value="{{option.id}}">{{option.name}}</option>
	                                     		</select>
	                                     </div>
                             </div>
							<div class="form-group <%=txtclass%>">
						   			    <label class="col-lg-2 control-label  req_lbl">Mobile No.</label>
	                        			<div class="col-lg-4"> 
	                         			 		 <input type="text"   name="nmobileNo" id="nmobileNo" class="form-control" ng-model="nmobileNo"  required="required" ng-pattern="/^[6-9][0-9]{9}$/" ng-change="validateuserragister('nmobileNo',' mobile number is already register with .','checkMobileNoExitOrNot')">
	                                     		   <span class="error" ng-show="userdetails.nmobileNo.$error.pattern || employeedetails.mobileNo.$error.required">Invalid Mobile Number!</span>
	                                     </div>
	                                     	<label class="col-lg-2 control-label" ng-show="nuserRoleId==105 || nuserRoleId==113"> Web Password</label>
		                         			<div class="col-lg-4" ng-show="nuserRoleId==105 || nuserRoleId==113"> 
		                         			 		 <input type="text"   name="npassword" id="npassword" class="form-control" ng-model="npassword" >
		                                     </div>
                             </div>
                                   <div></div>
                                <div class="form-group" style="margin-top: 4%;">
                                    <div class="col-lg-offset-5 col-lg-12">
                                    <input type="hidden" name="pkid" ng-model="pkid">
		                               		<button class="<%=Constant.btnsaveclass %>" type="button" ng-disabled="userdetails.$invalid" ng-click="saveUser()"><i class="<%=Constant.btniconsave%>" <%=Constant.btniconcolorwhite%>></i>  <%=Constant.btnsave%></button>
		                                    <a href="menu?url=viewUserList"><button class="btn btn-warning" type="button"> <i class="glyphicon glyphicon-eye-open" style="color: white;"></i> View Details</button></a>
                                    </div>
                                </div>
   						  
  					</div>
						</form>
						</div>
					
					  </section>
					 
                </div>
          </div>
                        </div>
            <!-- page end-->
</section>
 <!-- footer -->
		<%@include file="footer.jsp" %>
  <!-- / footer -->
</section>

</body>
</html>
