fetch.controller('getpartyinfo', function($scope, $http,$rootScope) {
	 $scope.getPartylist = function(partyName,partyNameList,isallparty,materialId) {
		  var   prodName=angular.element(document.getElementById(partyName));  
		  $scope.textbox = prodName.val();
		  
		//  alert( $scope.textbox)
		  if($scope.textbox.length>2)
			  {
			  $http({
				   method: 'POST',
				   url: 'findPartyListByName',
				   params: {partyName:$scope.textbox,isallparty:isallparty}
				  }).then(function successCallback(response) {
					  $scope.temp = angular.fromJson(response.data);
					  console.log($scope.temp);
					  $(partyNameList).empty();
						angular.forEach($scope.temp, function(value, key) {
							 $(partyNameList).append('<option id="'+value.partyId+'" data-address="'+value.address+'" data-stateId="'+value.stateId+'"  data-stateCode="'+value.stateCode+'" data-stateName="'+value.stateName+'"  value="'+value.partyName+'"></option>');
							});
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
						  });
	
			  }
	  }
	  $scope.getpartyid = function(partyName,partyId,isaddress) {
		  var   custName=angular.element(document.getElementById(partyName));  
		  $scope.textbox = custName.val();
		 var opt = $('option[value="'+$scope.textbox+'"]');
		 custid=opt.attr('id');
		 var address=opt.attr('data-address');
		 var stateName=opt.attr('data-stateName');
		 var stateId=opt.attr('data-stateId');
		 var stateCode=opt.attr('data-stateCode');
		if(custid!=undefined)
			{
			document.getElementById(partyId).value=custid;
			if(isaddress)
				{
					$scope.address=address;
					$scope.stateCode=stateCode
					$("#stateId").val(stateId); 
					$scope.stateName=stateName;
				}
					$("#"+partyName).css("background-color","#DAFBF1");
			}else{
				 $("#"+partyName).css("background-color","#FC9B9B");
				 $("#"+partyName).val(""); 
				 $("#"+partyName).focus(); 
				 document.getElementById(partyId).value=0;
			}
	  }
	
	});

