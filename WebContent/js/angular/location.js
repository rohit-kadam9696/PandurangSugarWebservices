fetch.controller('getlocation', function($scope, $http) {
	 $scope.getlocationlist = function(location,flocationlist) {
		  var   vechileno=angular.element(document.getElementById(location));  
		  $scope.textbox = vechileno.val();
		//  alert( $scope.textbox)
		  $(flocationlist).empty();
		  if($scope.textbox.length>2)
			  {
			  $http({
				   method: 'POST',
				   url: 'findLocationIdByName',
				   params: {locationName:$scope.textbox}
				  }).then(function successCallback(response) {
					  $scope.temp = angular.fromJson(response.data);
						angular.forEach($scope.temp, function(value, key) {
							 $(flocationlist).append('<option id="'+value.locationId+'" value="'+value.locationName+'"></option>');
							});
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
						  });
	
			  }
	  }
	  $scope.getlocationid = function(location,locationid) {
		  var   location1=angular.element(document.getElementById(location));  
		  $scope.textbox = location1.val();
		 var opt = $('option[value="'+$scope.textbox+'"]');
		 custid=opt.attr('id');
		if(custid!=undefined)
			{
			document.getElementById(locationid).value=custid;
			}else{
				document.getElementById(locationid).value=0;
			}
			
	  }
	});

