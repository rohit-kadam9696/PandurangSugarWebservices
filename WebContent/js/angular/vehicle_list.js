fetch.controller('getvechile', function($scope, $http) {
	
	$scope.getvechilelist = function() {
		  var   vechileno=angular.element(document.getElementById("vehicleNo"));  
		  $scope.textbox = vechileno.val();
		 if($scope.textbox.length>2)
			  {
			  $http.get("findVehicleList?vehicleNo="+$scope.textbox)
    	  .then(function(response) {
    		   $scope.temp = angular.fromJson(response.data);
    		   $("#vlist").empty();
						angular.forEach($scope.temp, function(value, key) {
							 $('#vlist').append('<option id="'+value.vehicleId+'" value="'+value.vehicleNumber+'"></option>');
							});
						
    	  });
			  }
	  }
	  $scope.getvechileid = function() {
		  var   vechileno=angular.element(document.getElementById("vehicleNo"));  
		  $scope.textbox = vechileno.val();
		 //alert($scope.textbox);
		 var opt = $('option[value="'+$scope.textbox+'"]');
		 //alert(opt);
		 custid=opt.attr('id');
		if(custid!=undefined)
			{
			document.getElementById("vechileid").value=custid;
			 $("#vehicleNo").css("background-color","#DAFBF1");
			}else{
				$scope.vehicleNo="";
				 $("#vehicleNo").css("background-color","#FC9B9B");
				 $("#vehicleNo").focus(); 
				document.getElementById("vechileid").value=0;
			}
			
	  }
	});

