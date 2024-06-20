var fetch = angular.module('myapp', []);
fetch.controller('systemuserdtl', function($scope, $http,$rootScope) {
	
	
	 $scope.getSugarTypeAndLocationList = function() {
		 if($scope.nuserRoleId!=undefined)
			{
			 $('#loading-image').show();
			 $http({
				   method: 'POST',
				   url: 'menu?url=getSugarTypeAndLocationList',
				   params: {nuserRoleId:$scope.nuserRoleId}
				  }).then(function successCallback(response) {
					  $scope.sugTypeList = response.data.typeList;
					  $scope.locationList = response.data.locationList;
					  $scope.selectLoader("nsugTypeId");
					  $scope.selectLoader("nlocationId");
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
						  });
			}
	  }
	 $scope.validateuserragister = function(scopelbl,error,url){
		 	var current=this;
			$scope.fieldvalue=current[scopelbl];
			 if($scope.fieldvalue!=undefined)
		     {
				  $http({
				   method: 'post',
				   url: 'menu?url='+url,
				   params: {fieldvalue:$scope.fieldvalue}
				  }).then(function successCallback(response) {
					 if(response.data.success==true){
						 alert(error);
						 current[scopelbl] = "";
						}}, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
				  });
		     }
		 };
		 $scope.saveUser = function(){
			 $http({
				   method: 'post',
				   url: 'menu?url=saveSystemUser',
				   params: {
					   vfullName:$scope.vfullName,
					   vfullNameLocal:$scope.vfullNameLocal,
					   nuserName:$scope.nuserName,
					   vuserName:$scope.vuserName,
					   ndeptId:$scope.ndeptId,
					   nuserRoleId:$scope.nuserRoleId,
					   nsugTypeId:$scope.nsugTypeId,
					   nlocationId:$scope.nlocationId,
					   nmobileNo:$scope.nmobileNo,
					   pkid:$scope.pkid,
					   }
				  }).then(function successCallback(response) {
					 if(response.data.success==true)
					 	{
						 	alert("Record Saved");
						}else
							{
							alert(response.data.msg);
							}
					 }, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
				  });
		 }
		 $scope.loadData = function() {
				 $('#loading-image').show();
				 $http({
					   method: 'POST',
					   url: 'menu?url=loadData',
					   params: {nuserRoleId:$scope.nuserRoleId}
					  }).then(function successCallback(response) {
						  $scope.roleList = response.data.roleList;
						  $scope.deptList = response.data.deptList;
						  $scope.selectLoader("nuserRoleId");
						  $scope.selectLoader("ndeptId");
						 }, function errorCallback(response) {
							 alert("Invalid Request Please Contact System Administrator!")
							  });
		  }
		 
		 $scope.getInfoById = function() {
			 if($scope.nuserName!=undefined)
				{
				 $('#loading-image').show();
				 $http({
					   method: 'POST',
					   url: 'menu?url=getInfoById',
					   params: {nuserName:$scope.nuserName}
					  }).then(function successCallback(response) {
						  if(response.data.success)
							  {
							  	$scope.vuserName=response.data.vuserName;
							  	$scope.vfullName=response.data.vfullName;
							  	$scope.vfullNameLocal=response.data.vfullNameLocal;
							 	$scope.ndeptId=""+response.data.ndeptId;
							  	$scope.nuserRoleId=""+response.data.nuserRoleId;
							  	$scope.getSugarTypeAndLocationList();
							  	$scope.nsugTypeId=response.data.nsugTypeId;
							  	$scope.nlocationId=response.data.nlocationId;
							  	$scope.nmobileNo=response.data.nmobileNo;
							  	$scope.pkid=2;
							  }else
								  {
								  	$scope.pkid=1;
								  }
						  $('#loading-image').hide();
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

