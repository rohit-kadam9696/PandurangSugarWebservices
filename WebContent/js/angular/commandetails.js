var fetch = angular.module('myapp', []);
fetch.controller('commandetails', ['$scope', '$http', function ($scope, $http,$timeout) 
{
	 $scope.init  = function(state_id,district_id) {
		 $scope.getdistrict(state_id);
		 $scope.getsubdistrict(district_id);
	 }
	
		$scope.isDisabled = false;
		 $scope.validateLogin= function(){
			 if($scope.yearId==undefined)
		     { 
				 userTypeerror.innerHTML="Please Select FInancial Year...";
				 $scope.isDisabled = true;
		     }else if($scope.name==undefined)
		     { 
		    	 $scope.isDisabled = true; 
		     }else
		    	 {
		    	 userTypeerror.innerHTML="";
		    	 $scope.isDisabled = false;
		    	 $scope.username=$scope.yearId+"###"+$scope.name;
		    	 }
		 };
	 
			 $scope.validateuserragister = function(scopelbl,error,url){
					var current=this;
					$scope.fieldvalue=current[scopelbl];
					 if($scope.fieldvalue!=undefined)
				     {
				  $http({
				   method: 'post',
				   url: url,
				   params: {fieldvalue:$scope.fieldvalue}
				  }).then(function successCallback(response) {
					 if(response.data==true){
						 alert(error);
						 current[scopelbl] = "";
						 //$("#"+scopelbl).val("");
						}}, function errorCallback(response) {
						 alert("Invalid Request Please Contact System Administrator!")
				  });
				     }
				 };
			
				 $scope.verifypassword = function() {
					 if($scope.newpass!=$scope.cfmpass)
						 {
						 $scope.invalidpass="Password do not match"; }else{
							 $scope.invalidpass="";
						 }
				 };
				 $scope.changePasss = function() {
					  $http({
						   method: 'post',
						   url: "changePasss",
						   params: {oldpass:$scope.oldpass,newpass:$scope.newpass}
						  }).then(function successCallback(response) {
							  if(response.data<0)
								 {
								   alert("Wrong old password");
					 			 }else if(response.data!=0)
								 {
									   alert("Password Successfully Changed");
						 		}else{
					 				alert("Password not Changed please try again");
								 }
							  $scope.oldpass="";
							  $scope.newpass="";
							  $scope.cfmpass="";
							 }, function errorCallback(response) {
								 alert("Invalid Request Please Contact System Administrator!")
								  });
				 };
				 
}]);

