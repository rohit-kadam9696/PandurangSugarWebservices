<%@page import="com.twd.pandurangsugar.web.bean.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title><%=WebConstant.projectName %> :: System User List</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../css_link.jsp" %>
<%@include file="../js_link.jsp" %>
<%@include file="../boostrapdatatable.jsp" %>
<%@include file="../datepicker.jsp" %> 
<script type="text/javascript" src="js/angular/list.js"> </script>
</head>
<body ng-app="myapp" ng-controller='list'>
<%--  --%>
<section id="container">
<%@include file="../header.jsp" %>
<!--header end-->

<%@include file="../menu.jsp" %>
<!--sidebar end-->
<!--main content start-->
	<section class="wrapper">
		<div class="form-w3layouts">
            <!-- page start-->
            <div class="row">
                  <div class="col-lg-12">
                    <section class="panel">
                	      <header class="panel-heading">
                          System User List
                            <span class="tools pull-right">
                                <a class="fa fa-chevron-down" href="javascript:;"></a>
                                <a class="fa fa-times" href="javascript:;"></a>
                             </span>
                        </header>
                        <div class="panel-body">
                         <div class="table-responsive">
                         <%@include file="../message.jsp" %>
                           <div class="table-responsive">
                           <table id="example" class="display" style="width:100%">
                            <thead>
                             <tr>
                                <th>Sr.No</th>
                                <th>Full Name</th>
                                <th>User Name</th>
                                <th>User Code</th>
                                <th>Department</th>
                                <th>Mobile Number</th>
                                <th>Status </th>
                             </tr> 
                            </thead> 
                              <tbody>
                              <%ArrayList<UserBean> viewUserList=(ArrayList)request.getAttribute("viewUserList");
                              if(viewUserList==null)
                            	  viewUserList=new ArrayList<>();
                              int count=1;
                              %>
                            	<%for(UserBean ubean:viewUserList){ %>
                            	<tr>
                            		<td><%=count++%></td>
                            		<td><%=ubean.getVfullName()%></td>
                            		<td><%=ubean.getVuserName()%></td>
                            		<td><%=ubean.getNuserName()%></td>
                            		<td><%=ubean.getDeptName()%></td>
                            		<td><%=ubean.getNmobileNo()%></td>
                            		<td>
                            		<button type="button" id="activedeactive<%=count%>"  class="btn btn-<%=ubean.getVactive()!=null?ubean.getVactive().equalsIgnoreCase("Y")?"success":"danger":""%>" ng-click="deleteInfo('activedeactiveUserById',<%=ubean.getNuserName()%>,<%=ubean.getVactive()%>,'activedeactive<%=count%>','example','update','want to active/deactive Bank')"><%=ubean.getVactive()!=null?ubean.getVactive().equalsIgnoreCase("Y")?"Active":"Deactive":""%></button>
                            		</td>
                            		<%-- <td>
                            		<button type="button" id="delete<%=count%>"  class="badge badge-danger" ng-click="deleteInfo('deleteUserById',<%=ubean.getNuserName()%>,<%=ubean.getVactive()%>,'delete<%=count%>','example','delete','want to delete Bank')"><i class="glyphicon glyphicon-trash" style='color: white;'></i>Delete</button>
                            		</td> --%>
                            	</tr>
                            	<%}%>
                               </tbody>
                                </table>
                              </div>
                              </div>
       					 </div>
                    </section>
                </div>
            </div>
           
                        </div>
                
</section>
 <!-- footer -->
		  <%@include file="../footer.jsp" %>
		   <%@include file="../boostrapalert.jsp" %>
  <!-- / footer -->
</section>


</body>
</html>