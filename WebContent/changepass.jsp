<%@page import="com.twd.pandurangsugar.both.constant.Constant"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="org.json.JSONArray"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html><html>
<head>
<title><%=WebConstant.projectName%>  | Change Password</title>
<%@include file="css_link.jsp" %>
<%@include file="js_link.jsp" %>
<%@include file="datepicker.jsp" %> 
<script type="text/javascript" src="js/angular/commandetails.js"> </script>   
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
                           <form class="form-horizontal"  ng-app="myapp" ng-controller='commandetails' action="saveDesignation" method="post" id="changepass" name="changepass"  autocomplete="off">
                            <div class="col-lg-12">
                          <header class="panel-heading">
						 Change Password
					</header>
					</div>
					 <div class="col-lg-12" style="margin-top: 1em">
					   		 <div class="form-group <%=txtclass%>">
                        			<label class="col-lg-3 control-label  req_lbl">Old Password</label>
                         			<div class="col-lg-4"> 
                         			 		 <input type="password"   name="oldpass" id="oldpass" class="form-control" ng-model="oldpass" required="required">
                                     		  <span class="error" ng-show="changepass.oldpass.$error.required">Old Password Required!</span>
                                     </div>
                              </div> 
                               <div class="form-group <%=txtclass%>">
                        			<label class="col-lg-3 control-label  req_lbl">New Password</label>
                         			<div class="col-lg-4"> 
                         			 		 <input type="password"   name="newpass" id="newpass" class="form-control" ng-model="newpass" required="required">
                                     		  <span class="error" ng-show="changepass.newpass.$error.required">New Password Required!</span>
                                     </div>
                              </div> 
                               <div class="form-group <%=txtclass%>">
                        			<label class="col-lg-3 control-label  req_lbl">Confirm Password</label>
                         			<div class="col-lg-4"> 
                         			 		 <input type="password"   name="cfmpass" id="cfmpass" class="form-control" ng-model="cfmpass" required="required" ng-keyup="verifypassword()">
                                     		  <span class="error" ng-show="changepass.cfmpass.$error.required">Confirm Password Required!</span>
                                     		  <span class="error" ng-model="invalidpass">{{invalidpass}}</span>
                                     </div>
                              </div>   
								<div class="form-group" style="margin-top: 4em">
                                    <div class="col-lg-offset-3 col-lg-12">
                               <button class="<%=Constant.btnsaveclass %>" type="button" ng-disabled="changepass.$invalid  || newpass !=cfmpass" ng-click="changePasss()"><i class="<%=Constant.btniconsave%>" <%=Constant.btniconcolorwhite%>></i>  <%=Constant.btnsave%></button>
                                   <a href="viewDesignation"><button class="btn btn-warning" type="button"> <i class="glyphicon glyphicon-eye-open" style="color: white;"></i> View Details</button></a>
                                   
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
<script type="text/javascript">
$(document).ready(function(){
  	 $('#attachment').ajaxfileupload({
  	      'action': 'UploadFile',	      	    
  	  'onComplete': function(response) {
  		$('#loading-image').hide();
  		Validatepdf($(response).text());
  	      }});

  	function Validatepdf(fileUpload) {
  	  	 var allowedFiles = ['.pdf','.jpg','.png'];
  	  	 var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(" + allowedFiles.join('|') + ")$");
  	     if (!regex.test(fileUpload.toLowerCase())) {
  	         alert("Please upload files having extensions:   " + allowedFiles.join(', ') + "    only.");
  	          return false;
  	     }else{
  	   	  document.getElementById("tender_pdf").value=fileUpload;
  	   	  document.getElementById("tender_pdfname").innerHTML=fileUpload;
  	     }
  	     return true;
  	  }
	  
	 $('#dataexcelfile').ajaxfileupload({
 	      'action': 'UploadExcelFile',	      	    
 	  'onComplete': function(response) {
 		$('#loading-image').hide();
 		Validateexcel($(response).text());
 	      }});
	 
	 
	   function Validateexcel(fileUpload) {
	   	 var allowedFiles = ['.xls'];
	   	 var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(" + allowedFiles.join('|') + ")$");
	      if (!regex.test(fileUpload.toLowerCase())) {
	          alert("Please upload files having extensions:   " + allowedFiles.join(', ') + "    only.");
	           return false;
	      }else{
	    	  document.getElementById("tender_excel").value=fileUpload;
	    	  document.getElementById("tender_excelname").innerHTML=fileUpload;
	      }
	      return true;
	   }
	   
  	 
  
});
</script>
</body>
</html>
