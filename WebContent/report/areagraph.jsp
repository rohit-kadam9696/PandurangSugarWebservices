<!DOCTYPE html>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
    <title>Simple Polygon</title>
    
    <% JSONObject job = (JSONObject) request.getAttribute("plantationMapReport");
    	JSONObject firstlatlong = job.has("firstlat") ? job.getJSONObject("firstlat") : new JSONObject() ;
    %>
     <script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>
    <script  src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAqbK_xrFSPEQ15g6C9-8wKP4wuwCmMtWU&callback=initMap&libraries=&v=weekly" defer></script>
    <style type="text/css">
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
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
    	   var myLatlng = new google.maps.LatLng(<%=firstlatlong.has("lat")?firstlatlong.getDouble("lat"):15.9977581308398%>, <%=firstlatlong.has("lng")?firstlatlong.getDouble("lng"):74.41087044775486%>);
    	      var map = new google.maps.Map(document.getElementById('map'), {
    	        zoom: 17,
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
	  var arr=<%=(job.has("main")?job.getJSONArray("main"):new JSONArray()).toString()%>;
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
    <div id="map"></div>
  </body>
</html>