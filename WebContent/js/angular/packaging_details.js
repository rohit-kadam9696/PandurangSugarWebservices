var fetch = angular.module('myapp', []);
fetch.controller('packaging_details', ['$scope', '$http', function ($scope, $http,$timeout) 
{
	 $scope.packaging_list = [];
	
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
		 var weight=$scope.weight_d;
		 var qtyInPc=$scope.qtyInPc_d;
		 var weightRange=parseFloat(weight)/parseFloat(qtyInPc);
		 $scope.weightRange_d=weightRange.toFixed(2);
	 }
	 $scope.getStockSizeList= function(materialId,colorId,productId) {
		 if((materialId==0 || materialId==undefined) && (colorId==0  || colorId==undefined) && (productId==0 || productId==undefined))
			 {
			 	materialId=$scope.materialId;
			 	colorId=$scope.colorId;
			 	productId=$("#productId").val();
			 }
		 if(materialId!=undefined && colorId!=undefined && productId!=undefined  && productId!=0)
		{
			 $('#loading-image').show();
		      	$http({
		              method: 'post',
		              url: 'getStockSizeList',
		              headers: {
		                  'X-Requested-With': 'XMLHttpRequest'
		              },
		              params: {
		            	  materialId: materialId,
		            	  colorId: colorId,
		            	  productId:productId
		              }
		          }).then(function successCallback(response) {
		        	 /* for(var i = 0; i < response.data.length; i++) {
		        		  var data=response.data[i];
		        		  $("#size").append('<option value="'+data+'" selected="">'+data+'</option>');
		        	  }*/
		        	    $("#size").selectpicker("refresh");
		        	  // $('#loading-image').hide();
		        	    $scope.size_list = response.data;
					  $scope.selectLoader("size");
		          }, function errorCallback(response) {
		         	 $('#loading-image').hide();
		         	 alert("Invalid Request Please Contact System Administrator!")
		         	  });
		}
	 }
	 $scope.getStockByMaterialProductColorSizeById= function(id){
		 var productId=$("#productId").val();
		 var size=$scope.size;
		 var materialId=$scope.materialId;
		 var colorId=$scope.colorId;
		 if(materialId!=undefined && colorId!=undefined && productId!=undefined && (size!=undefined  && size!='undefined' && size!=''))
			{
				 $('#loading-image').show();
			      	$http({
			              method: 'post',
			              url: 'getStockByMaterialProductColorSizeById',
			              headers: {
			                  'X-Requested-With': 'XMLHttpRequest'
			              },
			              params: {
			            	  materialId: materialId,
			            	  colorId: colorId,
			            	  productId:productId,
			            	  size:size,
			              }
			          }).then(function successCallback(response) {
			        	  var weight=parseFloat(response.data.weight);
			        	  var qty=parseFloat(response.data.qty);
			        	  if($("#"+id).val()!=0)
			        		  {
			        		  	weight=weight+parseFloat($scope.weight);
			        		  	qty=qty+parseFloat($scope.qtyInPc);
			        		  }
			        	  $scope.available_weight=weight.toFixed(2);
			        	  $scope.available_qtyInPc=qty.toFixed(2);
			        	  $('#loading-image').hide();
			        	  $scope.size_d=$scope.size;
			          }, function errorCallback(response) {
			         	 $('#loading-image').hide();
			         	 alert("Invalid Request Please Contact System Administrator!")
			         	  });
			}else
				{
					$scope.available_weight=0.00;
	        	    $scope.available_qtyInPc=0.00;
				}
	 }
	 
	 
	 $scope.selectLoader = function(id){
		 setTimeout(function () {
				 $('#'+id).addClass('selectpicker');
				 $('#'+id).attr('data-live-search', 'true');
				 $('#'+id).selectpicker('refresh');
				 $('.selectpicker').selectpicker('refresh');
				 $('#loading-image').hide();
				 $scope.getStockByMaterialProductColorSizeById();
			  },2000);
		 }
	 
	 $scope.addRow = function () {
		 if ($scope.materialId_d == undefined) {
			 alert("Invalid material type");
		 }else if ($("#productId_d").val() == undefined || $("#productName_d").val() == undefined) {
			 alert("Invalid product");
		 }else if ($scope.colorId_d == undefined) {
			 alert("Invalid Color");
		 }else if ($scope.weight_d== undefined ||  $scope.weight_d<=0) {
			 alert("weight Should Be Greater Than 0");
		 }else if ($scope.qtyInPc_d== undefined ||  $scope.qtyInPc_d<=0) {
			 alert("qtyInPc Should Be Greater Than 0");
		 }else if ($scope.weightRange_d== undefined ||  $scope.weightRange_d<=0) {
			 alert("Weight Range Should Be Greater Than 0");
		 }else{
			 var usedWeight=$scope.weight;
			 var usedqtyInPc=$scope.qtyInPc;
			 var availableWeight=$scope.available_weight;
			 var availableQtyInPc=$scope.available_qtyInPc;
			 var weight_d=$scope.weight_d;
			 var qtyInPc_d=$scope.qtyInPc_d;
			 availableWeight=parseFloat(availableWeight);
			 usedWeight=parseFloat(usedWeight)+parseFloat(weight_d);
			 
			 availableQtyInPc=parseFloat(availableQtyInPc);
			 usedqtyInPc=parseFloat(usedqtyInPc)+parseFloat(qtyInPc_d);
			/* if(usedWeight>availableWeight)
				 {
				 	alert("Stock Not Available");
				 	return false;
				 }*/
				 if(usedqtyInPc>availableQtyInPc)
				 {
				 	alert("Quantity Not Available");
				 	return false;
				 }
		 var packaging =[];
		   var duplicateentries=false;
		   
		   var unitId=1;
		   	 packaging.materialName = $("#materialId_d option:selected").text();
	    	 packaging.productName =  $("#productName_d").val();
	    	 packaging.colorName = $("#colorId_d option:selected").text();
	    	 packaging.size = $scope.size_d;
	    	 packaging.weight = $scope.weight_d;
	    	 packaging.qtyInPc = $scope.qtyInPc_d;
	    	 packaging.weightRange = $scope.weightRange_d;
	    	 packaging.materialId = $scope.materialId_d;
	    	 packaging.productId = $("#productId_d").val();
	    	 packaging.colorId = $scope.colorId_d;
	    	 packaging.unitId =unitId;
	    	 packaging.packagingDtlId =$scope.packagingDtlId;
	    	 $scope.packaging_list.push(packaging);
	    	 $scope.clearText();
	    	 $scope.calulateStock();
	    	 $scope.txtReadOnlyValidaton();
     }
 };
 
 $scope.txtReadOnlyValidaton = function () {
	var len= $scope.packaging_list.length;
	if(len>0)
		{
			$('#materialId').attr("readonly", true);
			$('#productName').attr("readonly", true);
			$('#colorId').attr("readonly", true);
			$('#size').attr("readonly", true);
		}else
			{
				$('#materialId').attr("readonly", false);
				$('#productName').attr("readonly", false);
				$('#colorId').attr("readonly", false);
				$('#size').attr("readonly", false);
			}
 }
 $scope.calulateStock = function () {
	 var weight=0;
	 var qtyInPc=0;
 	 angular.forEach($scope.packaging_list, function (value) { 
			   			weight=weight+parseFloat(value.weight);
			   			qtyInPc=qtyInPc+parseFloat(value.qtyInPc);
			   	});
 	$scope.weight=weight.toFixed(2);
	$scope.qtyInPc=qtyInPc.toFixed(2);
 }
 
    	 $scope.clearText = function () {
    		$("#productName_d").val("");
	    	//$scope.size_d="";
	    	$scope.weight_d="";
	    	$scope.qtyInPc_d="";
	    	$scope.weightRange_d="";
	    	$scope.materialId_d="";
	    	$("#productId_d").val("");
	    	$scope.colorId_d="";
	    	$scope.packagingDid="0";
    	 }
    	 $scope.clearProductText = function () {
    		 $("#productId").val("");
    		 $("#productName").val("");
    		 $('#size').addClass('selectpicker');
			 $('#size').attr('data-live-search', 'true');
			 $('#size').selectpicker('refresh');
			 $('.selectpicker').selectpicker('refresh');
    	 }
    	 
    	 $scope.removeRow = function (i) {
			 var result = confirm("Want to delete?");
			 if (result) {
				 if(!$scope.packaging_list[i].packagingDtlId)
					 {
						 $scope.packaging_list.splice(i, 1);
						 $scope.packagingDtlId=0;
						 $scope.calulateStock();
						 $scope.txtReadOnlyValidaton();
					 }else
						 {
							 $('#loading-image').show();
					         	$http({
					                 method: 'post',
					                 url: 'deletePackagingDetailById',
					                 headers: {
					                     'X-Requested-With': 'XMLHttpRequest'
					                 },
					                 params: {
					                	 packagingDtlId: $scope.packaging_list[i].packagingDtlId,
					                	 packagingId: $("#packagingId").val(),
					                 }
					             }).then(function successCallback(response) {
					            	 var val = response.data;
					            	 if(val>0)
					            		 {
						            		 $scope.packaging_list.splice(i, 1);
						        			 $scope.packagingDtlId=0;
						        			 $scope.calulateStock();
						        			 $scope.txtReadOnlyValidaton();
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
  var packaging = [];
    angular.forEach($scope.packaging_list, function (value) { 
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
   			'packagingDtlId':value.packagingDtlId,
   		 }
   	packaging.push(temp);
    });
    
    if(packaging=="")
	 {
   	 alert("Please Add atleast one product....")
   	 return false;
	 }else{
     $("#jsonstring").val(JSON.stringify(packaging));
    }
	 
};
	
}]);

