fetch.controller('productInfo', function($scope, $http,$rootScope) {
	 $scope.getProductlist = function(productName,productlist,isallproduct) {
		  var   custName=angular.element(document.getElementById(productName)); 
		  var   materialId=$("#materialId").val();  
		  $scope.textbox = custName.val();
		//  alert( $scope.textbox)
		  if($scope.textbox.length>2)
			  {
			  $http({
				   method: 'POST',
				   url: 'findProductListByName',
				   params: {productName:$scope.textbox,isallproduct:isallproduct,materialId: materialId}
				  }).then(function successCallback(response) {
					  $scope.temp = angular.fromJson(response.data);
					  $(productlist).empty();
						angular.forEach($scope.temp, function(value, key) {
							 $(productlist).append('<option id="'+value.productId+'" value="'+value.productName+'"></option>');
							});
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
						  });
	
			  }
	  }
	  $scope.getProductid = function(name,id) {
		  var   custName=angular.element(document.getElementById(name));  
		  $scope.textbox = custName.val();
		 var opt = $('option[value="'+$scope.textbox+'"]');
		 productId=opt.attr('id');
		if(productId!=undefined)
			{
			document.getElementById(id).value=productId;
					//$("#"+name).css("background-color","#DAFBF1");
			}else{
				 /*$("#"+name).css("background-color","#FC9B9B");
				 $("#"+name).val(""); 
				 $("#"+name).focus(); */
				 document.getElementById(id).value=0;
			}
	  }
	  $scope.saveProduct = function() {
		  $http({
			   method: 'POST',
			   url: 'saveProductDtl',
			   params: {productName:$scope.productName,materialId: $scope.materialId}
			  }).then(function successCallback(response) {
				  $('#saveProduct').modal('hide');
				 }, function errorCallback(response) {
					 alert("Invalid Request Please Contact System Administrator!")
					  });
	  }
	  
	
	});

