var fetch = angular.module('myapp', []);
fetch.controller('list', ['$scope', '$http', function ($scope, $http,$timeout) 
{
	 $scope.deleteInfo = function(url1,id,status,buttonId,tableId,text,msg) {
		 if($("#"+buttonId).html()=="Active")
		 {
		 		status='N';
		 }else
			 {
			 	status='Y';
			 }
		  swal({
              title: "Are you sure?",
              text: msg,
              type: "error",
              confirmButtonClass: 'btn-success',
              showCancelButton: true,
              closeOnConfirm: true,
              showLoaderOnConfirm: true
                 }, function () {
                   setTimeout(function () {
                       $http({
        				   method: 'POST',
        				   url: "menu?url="+url1,
        				   params: {id:id,status:status}
        				  }).then(function successCallback(response) {
        					 if(response.data.success)
        						 {
        						 		alert("Successfully Changed");
        						 			 if($("#"+buttonId).html()=="Active")
        									 {
        									 	status='N';
        									 	$("#"+buttonId).html("Deactive");
        									 	$("#"+buttonId).removeClass('btn-success');
        									 	$("#"+buttonId).addClass('btn-danger');
        									  }else
        										 {
        										  	status='Y';
        										 	$("#"+buttonId).removeClass('btn-danger');
         									 		$("#"+buttonId).addClass('btn-success');
         									 		$("#"+buttonId).html("Active");
        										}
        								 /*else
        									 {
        										 $("#"+buttonId).closest('tr').remove()
        									 }*/
        						 }else
        							 {
        							 	alert("Not Changed Please try again");
        							 }
        					 }, function errorCallback(response) {
        						 alert("Invalid Request Please Contact System Administrator!")
        						  });
                   }, 2);
                 });
		
		 
	}
}]);



