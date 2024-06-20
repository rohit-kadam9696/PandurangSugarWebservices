<%@page import="com.twd.pandurangsugar.both.constant.WebConstant"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collection"%>
<aside>
   <div id="sidebar" class="nav-collapse">
        <!-- sidebar menu start-->
        <div class="leftside-navigation">
            <ul class="sidebar-menu" id="nav-accordion">
                 <%
                
                 if(session!=null){
	                    	JSONObject list = (JSONObject) session.getAttribute("webPermission");
	              if(list!=null){%>
	                    	
	            	  <li>
	                    <a href="e_tender?url=home">
	                        <i class="fa fa-dashboard"></i>
	                        <span>Dashboard</span>
	                    </a>
	                </li>
	            	  
	            	  <%Iterator itr = list.keys();
                    		ArrayList<Integer> arr = new ArrayList<Integer>();           		
                    		while(itr.hasNext()){
                    			arr.add(Integer.parseInt((String)itr.next()));
                    		}
                    		Collections.sort(arr);
                    		for(int i = 0; i<arr.size(); i++){
                    			JSONObject job = list.getJSONObject(""+arr.get(i));
                    			%>
                <li class="sub-menu">
                    <a href="<%=job.getString("permission_url")%>">  <!-- javascript:; -->
                        <i class="fa fa-book"></i>
                        <span> <%=job.getString("permission_name")%></span>
                    </a>
                     <%if(job.has("child")){
			                            	Iterator itrinner = job.getJSONObject("child").keys();
			                            	 ArrayList<Integer> arr1 = new ArrayList<Integer>();           		
	                    		while(itrinner.hasNext()){
	                    			arr1.add(Integer.parseInt((String)itrinner.next()));
	                    		}
	                    		Collections.sort(arr1);
	                    		%>
                    <ul class="sub">
                    <%for(int i1 = 0; i1<arr1.size(); i1++){
			                            		JSONObject njob = job.getJSONObject("child").getJSONObject(""+arr1.get(i1));
			                            		%>
						<li><a href="<%=njob.getString("permission_url") %>"><%=njob.getString("permission_name") %></a></li>
						<%} %>
                    </ul>
                    <%} %>
                </li>
                 <%}}}%>
            </ul>            </div>
        <!-- sidebar menu end-->
    </div>
</aside>
<section id="main-content" class="">