var fetch = angular.module('myapp', []);
fetch.controller('inward_details', ['$scope', '$http', function ($scope, $http,$timeout) 
{
	 $scope.inward_list = [];
	
	 $('#invoiceTypeId').on('change', function() {
		 var prefix=$(this).find(':selected').attr('data-prefix');
		 var val=$(this).find(':selected').attr('value');
		 		$("#invoicePrefix").val(prefix); 
		 		if(val==2)
		 			{
		 				$scope.rate=0;
		 				$scope.subTotal=0;
		 			}
		 	});
	 $scope.calculateWeightRange= function() {
		 var weight=$scope.weight;
		 var qtyInPc=$scope.qtyInPc;
		 var weightRange=parseFloat(weight)/parseFloat(qtyInPc);
		 $scope.weightRange=weightRange.toFixed(2);
	 }
	 $scope.addRow = function () {
		 if ($scope.materialId == undefined) {
			 alert("Invalid material type");
		 }else if ($("#productId").val() == undefined || $("#productName").val() == undefined) {
			 alert("Invalid product");
		 }else if ($scope.colorId == undefined) {
			 alert("Invalid Color");
		 }else if ($scope.weight== undefined ||  $scope.weight<=0) {
			 alert("weight Should Be Greater Than 0");
		 }else if ($scope.qtyInPc== undefined ||  $scope.qtyInPc<=0) {
			 alert("qtyInPc Should Be Greater Than 0");
		 }else if ($scope.weightRange== undefined ||  $scope.weightRange<=0) {
			 alert("Weight Range Should Be Greater Than 0");
		 }else{
		 var inward =[];
		   var duplicateentries=false;
		   var unitId=1;
		   	 
		   	 inward.materialName = $("#materialId").text();
	    	 inward.productName =  $("#productName").val();
	    	 inward.colorName = $("#colorId").text();
	    	 inward.size = $scope.size;
	    	 inward.weight = $scope.weight;
	    	 inward.qtyInPc = $scope.qtyInPc;
	    	 inward.weightRange = $scope.weightRange;
	    	 inward.materialId = $scope.materialId;
	    	 inward.productId = $("#productId").val();
	    	 inward.colorId = $scope.colorId;
	    	 inward.unitId =unitId;
	    	 inward.invoiceDid =$scope.invoiceDid;
	    	 $scope.inward_list.push(inward);
	    	 $scope.clearText();   
     }
 };
 
    	 $scope.clearText = function () {
    		$("#productName").val("");
	    	$scope.size="";
	    	$scope.weight="";
	    	$scope.qtyInPc="";
	    	$scope.weightRange="";
	    	$scope.materialId="";
	    	$("#productId").val("");
	    	$scope.colorId="";
	    	$scope.invoiceDid="0";
    	 }
    	 $scope.removeRow = function (i) {
			 var result = confirm("Want to delete?");
			 if (result) {
				 if(!$scope.inward_list[i].inwardDtlId)
					 {
						 $scope.inward_list.splice(i, 1);
						 $scope.inwardDtlId=0;
					 }else
						 {
							 $('#loading-image').show();
					         	$http({
					                 method: 'post',
					                 url: 'deleteInwardDetailById',
					                 headers: {
					                     'X-Requested-With': 'XMLHttpRequest'
					                 },
					                 params: {
					                	 inwardDtlId: $scope.inward_list[i].inwardDtlId,
					                 }
					             }).then(function successCallback(response) {
					            	 var val = response.data;
					            	 if(val>0)
					            		 {
					            		 $scope.inward_list.splice(i, 1);
					        			 $scope.inwardDtlId=0;
					            		 }
					            	 $('#loading-image').hide();
					             }, function errorCallback(response) {
					            	 $('#loading-image').hide();
					            	 alert("Invalid Request Please Contact System Administrator!")
					            	  });
						 }
				
			 	}
			 };
	
//FINALLY SUBMIT THE DATA.
$scope.submit = function () {
  var inward = [];
    angular.forEach($scope.inward_list, function (value) { 
   	temp={
   			'size':value.size,
   			'weight':value.weight,
   			'qtyInPc':value.qtyInPc,
   			'weightRange':value.weightRange,
   			'materialId':value.materialId,
   			'productName':value.productName,
   			'productId':value.productId,
   			'colorId':value.colorId,
   			'unitId':value.unitId,
   			'inwardDtlId':value.inwardDtlId,
   		 }
   		inward.push(temp);
    });
    
    if(inward=="")
	 {
   	 alert("Please Add atleast one product....")
   	 return false;
	 }else{
     $("#jsonstring").val(JSON.stringify(inward));
    }
	 
};
	
}]);

