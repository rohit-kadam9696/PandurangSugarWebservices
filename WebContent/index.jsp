<!DOCTYPE html>
<%@page import="com.twd.pandurangsugar.both.constant.WebConstant"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.json.JSONObject"%>
<html>
<head>
<title>Login :: <%=WebConstant.projectName%></title>

<%@include file="css_link.jsp" %>
<%@include file="js_link.jsp" %>
</head>
<body>
<section id="container">
<section class="wrapper">
		<div class="form-w3layouts">
            <!-- page start-->
            <div class="row">
              <img src="website/img/englogo.png" alt="" class="img img-responsive">
              <br><br>
               <div class="col-lg-12">
                               		<h3 class="error" align="center"><%=session.getAttribute("login_error")==null?"":session.getAttribute("login_error")%>
									 <%session.removeAttribute("login_error");%> 
									</h3>
									</div>
            <div class="col-lg-4"></div>
                <div class="col-lg-5">
                    <section class="panel">
                     <div class="panel-body" >
                      <header class="panel-heading">
						Login Details
					</header>
					 <div class="col-lg-1"></div>
					 <div class="col-lg-9">
					       <form class="form-horizontal" action="weblogin" method="post" >
					       		<br><br>
					       		<div class="form-group">
                        			  <label class="col-lg-4 control-label  req_lbl">Username</label>
                         			   <div class="col-lg-8"> 
                                      		<input type="text" class="form-control" name="username" placeholder="USERNAME" required="">
                                       </div>
                               </div>
                               
                               <div class="form-group">
                        			  <label class="col-lg-4 control-label  req_lbl">Password</label>
                         			   <div class="col-lg-8"> 
                                      		<input type="password" name="password" id="password" class="form-control" required="required">
                                       </div>
                               </div>
                               <!-- <div class="col-lg-12 form-group"> <input type="checkbox" id="termsandcondition"  required=""><a href="#" data-toggle="modal" data-target="#myModal"> &nbsp;I&nbsp;Accept&nbsp;Terms&nbsp;&&nbsp;Conditions.</a></div> -->
                              
                               <div class="form-group">
                               <div class="col-lg-4"></div> 
                               <div class="col-lg-8">
                                <button type="submit" class="btn btn-primary btn-block">Login</button>
                                </div>
                               	</div>
                               	
								<h3 style="color: red;" align="center">${SPRING_SECURITY_LAST_EXCEPTION.message}</h3>
					       </form>
					      </div>
					      	
					    <!--   <div class="col-lg-8"><a href="clientuserlogin?url=findUserName">Find UserName</a></div>
                          <div class="col-lg-4"><a href="clientuserlogin?url=forgotpassword">Forgot&nbsp;Password?</a></div>
                          <div class="col-lg-12"><a href="samplefile/scsskl_tender_user_manual.pdf" target="blank">Download user Manual</a></div> -->
					      <div class="col-lg-12">
					        <%@include file="footer.jsp" %>
					      </div>
					 </div>
                    </section>
                    </div>
                    </div>
                    </div>
                    </section>
                    
</section>





<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Terms and Conditions</h4>
      </div>
      <div class="modal-body tendertermsandConditions_foregroundcolor">
        <%JSONObject obj=(JSONObject)request.getAttribute("tendertermsandConditions");
        if(obj==null)
        {
        	obj=new JSONObject();
        }
        %>
                        <%=obj.has("print_format_vendor_login_terms_condition")?obj.getString("print_format_vendor_login_terms_condition"):""%>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>
</body>
</html>
