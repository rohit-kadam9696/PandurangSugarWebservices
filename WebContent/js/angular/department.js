var fetch = angular.module('myapp', []);
fetch.controller('department', function($scope, $http,$rootScope) {
	 $scope.loadVillageBySection = function(id) {
		 if(id==0)
			 {
			 	id=$scope.nsectionId
			 }
		 if(id!=undefined)
			{
			 $('#loading-image').show();
			 $http({
				   method: 'GET',
				   url: 'menu?url=loadVillageBySection',
				   params: {nsectionId:id}
				  }).then(function successCallback(response) {
					  $scope.villageList = response.data;
					  $scope.selectLoader("nvillageId");
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
						  });
			}
	  }
	
	
	 
	 $scope.loadFarmerByVillege = function(id) {
		 if(id==0)
		 {
		 	id=$scope.nvillageId
		 }
		 if(id!=undefined)
			{
			 $('#loading-image').show();
			 $http({
				   method: 'GET',
				   url: 'menu?url=loadFarmerByVillege',
				   params: {nvillageId:id,yearId:$("#yearId").val()}
				  }).then(function successCallback(response) {
					  $scope.farmerList = response.data;
					  $scope.selectLoader("nfarmerCode");
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
						  });
			}
	  }
	 $scope.selectLoader = function(id){
		 setTimeout(function () {
				 $('#'+id).addClass('selectpicker');
				 $('#'+id).attr('data-live-search', 'true');
				 $('#'+id).selectpicker('refresh');
				 $('.selectpicker').selectpicker('refresh');
				 $('#loading-image').hide();
			  },2000);
		 }
	 
	
	});

