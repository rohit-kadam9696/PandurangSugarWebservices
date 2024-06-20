fetch.controller('getpumpinfo', function($scope, $http,$rootScope) {
	 $scope.getPumplist = function() {
		  if($scope.pumpName.length>2)
			  {
			  $http({
				   method: 'POST',
				   url: 'findPumpByName',
				   params: {pumpName:$scope.pumpName}
				  }).then(function successCallback(response) {
					  $scope.temp = angular.fromJson(response.data);
					  $("#pumplist").empty();
						angular.forEach($scope.temp, function(value, key) {
							 $("#pumplist").append('<option id="'+value.pumpId+'"  value="'+value.pumpName+'"></option>');
							});
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
						  });
	
			  }
	  }
	  $scope.getpumpid = function() {
		 var opt = $('option[value="'+$scope.pumpName+'"]');
		 		empid=opt.attr('id');
		if(empid!=undefined)
			{
				document.getElementById("pumpId").value=empid;
				$("#pumpName").css("background-color","#DAFBF1");
			}else{
				 $("#pumpName").css("background-color","#FC9B9B");
				 $("#pumpName").val(""); 
				 document.getElementById("pumpId").value=0;
			}
	  }
	
	});

