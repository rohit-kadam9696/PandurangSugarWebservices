 <% 
                     	String txtclass="has-success";
                     if(session.getAttribute("message")==null){session.setAttribute("result", "-1");}
                                          if(session.getAttribute("result").toString().equalsIgnoreCase("-1")){}else{
                     %>
                     <div class="sweet-alert alert <%=session.getAttribute("alertclass")%> myAlert-top" role="alert" id="success-alert">
                  	 <button type="button" class="close" data-dismiss="alert">x</button>
                   	 <strong><%=session.getAttribute("message")%></strong>
					 </div><%
					}if( session.getAttribute("result").toString().equalsIgnoreCase("0")){txtclass="has-error";}
				%>
				<%session.removeAttribute("alertclass"); session.removeAttribute("message");session.setAttribute("result", "-1");
					%>
					 <%@include file="loaderimg.jsp" %>	