fetch.controller('getcompanyinfo', function($scope, $http,$rootScope) {
	 $scope.getcompanylist = function(customerName,customerNameList,isallcustomer) {
		  var   custName=angular.element(document.getElementById(customerName));  
		  $scope.textbox = custName.val();
		//  alert( $scope.textbox)
		  if($scope.textbox.length>2)
			  {
			  $http({
				   method: 'POST',
				   url: 'findCustomerIdByName',
				   params: {custName:$scope.textbox,isallcustomer:isallcustomer}
				  }).then(function successCallback(response) {
					  $scope.temp = angular.fromJson(response.data);
					  console.log($scope.temp);
					  $(customerNameList).empty();
						angular.forEach($scope.temp, function(value, key) {
							 $(customerNameList).append('<option id="'+value.custId+'" data-address="'+value.address+'" data-stateId="'+value.stateId+'"  data-stateCode="'+value.stateCode+'" data-stateName="'+value.stateName+'"  data-bankId="'+value.bankId+'" value="'+value.custName+'"></option>');
							});
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
						  });
	
			  }
	  }
	  $scope.getcompanyid = function(customerName,customerId,isaddress) {
		  var   custName=angular.element(document.getElementById(customerName));  
		  $scope.textbox = custName.val();
		 var opt = $('option[value="'+$scope.textbox+'"]');
		 custid=opt.attr('id');
		 var address=opt.attr('data-address');
		 var stateName=opt.attr('data-stateName');
		 var stateId=opt.attr('data-stateId');
		 var stateCode=opt.attr('data-stateCode');
		 var bankId=opt.attr('data-bankId');
		if(custid!=undefined)
			{
			document.getElementById(customerId).value=custid;
			if(isaddress)
				{
					$scope.address=address;
					$scope.stateCode=stateCode
					$("#stateId").val(stateId); 
					$scope.showStateGSTField(stateCode);
					$scope.stateName=stateName;
				}
			$scope.bankId=bankId;
			$("#"+customerName).css("background-color","#DAFBF1");
			}else{
				 $("#"+customerName).css("background-color","#FC9B9B");
				 $("#"+customerName).val(""); 
				 $("#"+customerName).focus(); 
				document.getElementById(customerId).value=0;
			}
	  }
	  $scope.showStateGSTField = function(stateCode) {
		  if(stateCode=="27")
			{
				$rootScope.taxId="MH";
			}else if(stateCode=="35" || stateCode=="26" || stateCode=="1" || stateCode=="31"  || stateCode=="4"  || stateCode=="7"  || stateCode=="34")
				{
					$rootScope.taxId="OT";
				}else
					{
					$rootScope.taxId="IS";
					}
	  	}
	});

