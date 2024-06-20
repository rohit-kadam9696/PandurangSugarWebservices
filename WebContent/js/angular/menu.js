var app = angular.module('spssklweb', ["ngRoute"]);

app.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.
	  when('/systemuser', {
		  templateUrl: 'adduser.html',
		  controller: 'cntrspssklweb'
	  }).
	  when('/about', {
	    templateUrl: 'about.html',
	  }).
	  when('/services', {
	    templateUrl: 'services.html',
	  }).
	  when('/contact', {
	    templateUrl: 'contact.html',
	  })
	}])


	app.controller('cntrspssklweb', [function() {
	  console.log('this is mainctrl');
	}]);
/*
fetch.controller('cntrspssklweb', function($scope, $http,$rootScope) {
	$scope.viewPage = function(url) {
		
		if(url!="#")
			{
				var isSession=false;
				 $http({
				   method: 'POST',
				   url: 'UserSession',
				  }).then(function successCallback(response) {
					 alert(response.data.success)
					 if(response.data.success)
					 	{
						 	$("#maindiv").empty(); 
						 	if(url=="systemuser")
								{
						 			//$("#maindiv").load("js/angular/systemuser.js");
						 			  $("#maindiv").load("adduser.html");
								}
						 }else
							 {
							 alert("else")
							 	window.location.href = 'logout';
							 }	
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!");
						 isSession=false;
						  });
				}
		 
	}
	
	 $scope.test = function() {
		  alert("calll")
	  }
});*/


