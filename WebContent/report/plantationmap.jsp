<%@page import="com.twd.pandurangsugar.both.constant.Constant"%>
<%@page import="com.twd.pandurangsugar.android.bean.KeyPairBoolData"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.twd.pandurangsugar.web.bean.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title><%=WebConstant.projectName %> :: Plantation Map Report</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../css_link.jsp" %>
<%@include file="../js_link.jsp" %>
<%@include file="../boostrapdatatable.jsp" %>
<%@include file="../datepicker.jsp" %> 
<script type="text/javascript" src="js/angular/department.js"> </script>

<% JSONObject mapReport = (JSONObject) request.getAttribute("plantationMapReport");
if(mapReport==null)
{
	mapReport=new JSONObject();
}
    	JSONObject firstlatlong = mapReport.has("firstlat") ? mapReport.getJSONObject("firstlat") : new JSONObject() ;
    %>
    <%ArrayList<KeyPairBoolData> sectionList=(ArrayList)request.getAttribute("sectionList");
			if(sectionList==null)
			{
				sectionList=new ArrayList<KeyPairBoolData>();
			}
			ArrayList<String> yearList=(ArrayList)request.getAttribute("yearList");
			if(yearList==null)
			{
				yearList=new ArrayList<String>();
			}	
			String currentYear=(String)request.getAttribute("currentYear");
			if(currentYear==null)
				currentYear="";
			
			String nsectionId=(String)request.getAttribute("nsectionId");
			if(nsectionId==null)
				nsectionId="";
			
			String nfarmerCode=(String)request.getAttribute("nfarmerCode");
			if(nfarmerCode==null)
				nfarmerCode="";
			
			String nvillageId=(String)request.getAttribute("nvillageId");
			if(nvillageId==null)
				nvillageId="";
			%>
     <script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>
    <script  src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAqbK_xrFSPEQ15g6C9-8wKP4wuwCmMtWU&callback=initMap&libraries=&v=weekly" defer></script>
    <style type="text/css">
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 500px;
      }

      /* Optional: Makes the sample page fill the window. */
      html,
      body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
    </style>
    <script>
      (function(exports) {
        "use strict";

        // This example creates a simple polygon representing the Bermuda Triangle.
       function initMap() {
    	   var myLatlng = new google.maps.LatLng(<%=firstlatlong.has("lat")?firstlatlong.getDouble("lat"):17.851078989938735%>, <%=firstlatlong.has("lng")?firstlatlong.getDouble("lng"):75.10234051172793%>);
    	      var map = new google.maps.Map(document.getElementById('map'), {
    	        zoom: 18,
    	        center: myLatlng,
    	        mapTypeId: google.maps.MapTypeId.HYBRID
    	      });


    function addPoly(coorArray,sColor,sOpacity,weight,fColor,fOpacity)
    {
      var poly = new google.maps.Polygon({
      paths: coorArray,
        strokeColor: sColor,
        strokeOpacity: sOpacity,
        strokeWeight: weight,
        fillColor: fColor,
        fillOpacity: fOpacity,
		 title: "Click to zoom"
	  });
      return poly;
    };

      // Define the LatLng coordinates for the polygon's path.
	  
	  /* var arr=[{"lat":15.9977581308398,"lng":74.41087044775486,"name":"1616041223381@61","poli":[{"lat":15.9977581308398,"lng":74.41087044775486}	,{"lat":15.9981258653472,"lng":74.41082384437323} ,{"lat":15.998082678332338,"lng":74.41109374165535} ,{"lat":15.997826456671497,"lng":74.41114202141762}]
	  },{"lat":15.8724461,"lng":74.24316767603158,"name":"This is the  secret  message ---- 1","poli":[{"lat":15.8724461,"lng":74.1710937}	,{"lat":15.8723773,"lng":74.1711673} ,{"lat":15.8725167,"lng":74.1714596} ,{"lat":15.8726142,"lng":74.1714329} ,{"lat":15.8726099,"lng":74.1713704} ,{"lat":15.8725852,"lng":74.1713222} ,{"lat":15.8725462,"lng":74.1710891}]}]; */
	  var arr=<%=(mapReport.has("main")?mapReport.getJSONArray("main"):new JSONArray()).toString()%>;
	  for (var i in arr)
	  {
	  
	   var s1 = addPoly(arr[i].poli,'red',0.8,2,'red',.55);
       s1.setMap(map);
	   var secretMessages = arr[i].name;
            var marker = new google.maps.Marker({
              position: {
                lat: arr[i].lat,
                lng: arr[i].lng
              },
              map: map
            });
            attachSecretMessage(marker, secretMessages);
			function attachSecretMessage(marker, secretMessage) {
          var infowindow = new google.maps.InfoWindow({
            content: secretMessage
          });
          marker.addListener("click", function() {
            infowindow.open(marker.get("map"), marker);
          });
          s1.addListener("mouseover", function() {
              infowindow.open(marker.get("map"), marker);
            });
          s1.addListener("mouseout", function() {
              infowindow.close();
            });
        }
		 exports.attachSecretMessage = attachSecretMessage;
	  }}
	exports.initMap = initMap;
      })((this.window = this.window || {}));
    </script>
</head>
<body>
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
                          Plantation Map Report
                           <span style="float: left;">
	                	      <button class="btn btn-default" data-toggle="modal" data-target="#myModal">Change Parameter</button>
	                	   </span>
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
                           <tbody>
                             	<tr>
                            		<td id="map"></td>
                            		</tr>
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
<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Change Parameter</h4>
      </div>
      <form class="form-horizontal"  action="menu" ng-app="myapp" ng-controller='department'  method="get" id="viewMapReport" name="viewMapReport">
		      <div class="modal-body" ng-init="loadVillageBySection(<%=nsectionId%>);loadFarmerByVillege(<%=nvillageId%>)">
		       <div class="form-group">
		      <label class="col-lg-3 control-label  req_lbl">गळीत हंगाम</label>
		         	    	<div class="col-lg-7">
			        			 <select name="yearId" id="yearId" ng-model="yearId"  class="form-control selectpicker" ng-init="yearId='<%=currentYear%>'" required="required" data-live-search="true">
	                                     		  <option value="" disabled>Select your option</option>
	                                     		  		<%for(String  yearId:yearList){ %>
	                                     		  				<option value="<%=yearId%>"><%=yearId%></option>
	                                     		  		<%}%>
	                                     		  </select>
			        		</div>
			        		</div>
			        		<div class="form-group">
			        		 <label class="col-lg-3 control-label  req_lbl">गट</label>
		         	    	<div class="col-lg-7">
			        			 <select name="nsectionId" id="nsectionId" ng-model="nsectionId"  class="form-control selectpicker"  ng-init="nsectionId='<%=nsectionId%>'"  required="required" data-live-search="true" ng-change="loadVillageBySection()">
	                                     		  <option value="" disabled>Select your option</option>
	                                     		  		<%for(KeyPairBoolData data:sectionList){ %>
	                                     		  				<option value="<%=data.getId()%>"><%=data.getName()%></option>
	                                     		  		<%}%>
	                                     		  </select>
			        		</div>
			        		</div>
			        		<div class="form-group">
			        		 <label class="col-lg-3 control-label  req_lbl">गाव</label>
		         	    	<div class="col-lg-7">
			        			<select name="nvillageId" id="nvillageId" class="form-control selectpicker" ng-model="nvillageId"  ng-init="nvillageId='<%=nvillageId%>'"  required="required" data-live-search="true" ng-change="loadFarmerByVillege()">
	                                     		  <option value="" disabled>Select your option</option>
	                                     		  <option ng-repeat="option in villageList" value="{{option.id}}">{{option.name}}</option>
	                                     		  </select>
			        		</div>
			        		</div>
			         <div class="form-group">
			        		 <label class="col-lg-3 control-label  req_lbl">शेतकरी</label>
		         	    	<div class="col-lg-7">
			        			<select id="nfarmerCode" class="form-control selectpicker" ng-model="nfarmerCode"  ng-init="nfarmerCode='<%=nfarmerCode%>'"  required="required" data-live-search="true">
	                                     		  <option value="" disabled>Select your option</option>
	                                     		  <option ng-repeat="option in farmerList" value="{{option.id}}">{{option.name}}</option>
	                                     		  </select>
			        		</div>
			        		</div>
		      </div>
		      <div class="modal-footer">
		     			<input type="hidden" name="url" value="viewPlantationMapReport">
		     			<input type="hidden" name="nfarmerCode" id="nfarmerCode1">
	                             <button class="<%=Constant.btnsaveclass %>" type="button" onclick="viewReport()"><i class="<%=Constant.btniconview%>" <%=Constant.btniconcolorwhite%>></i>  <%=Constant.btnview%></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		      </div>
		</form>
    </div>

  </div>
</div>
<script type="text/javascript">
function viewReport()
{
	var nfarmerCode = $('#nfarmerCode').val();
	document.getElementById("nfarmerCode1").value=nfarmerCode;
	 $("#viewMapReport").submit();
	}
</script>

</body>
</html>