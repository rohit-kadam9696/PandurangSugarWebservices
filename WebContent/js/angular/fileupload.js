function uploadFile(id,fileext,inputtxtId){
	
	var formdata = new FormData();
	//alert(document.getElementById("uploadFileName"+id).files[0]);
	formdata.append("documentFileName", document.getElementById("uploadFileName"+id).files[0]);
    		$.ajax({
				type:"POST",
				url:"uploadFile",
				enctype: 'multipart/form-data',
				processData: false, //prevent jQuery from automatically transforming the data into a query string
		        contentType: false,
		        cache: false,
		        timeout: 600000,
				data:formdata,
				//alert("Uploaded");
				success: function(feedback){
					Validatepdf(feedback,fileext,inputtxtId);
				},
				error: function (e) {
					//alert("in error")
		            console.log("ERROR : ", e);
		        }
			});
}



function Validatepdf(fileUpload,fileext,inputtxtId) {
	if(fileUpload=="")
	{
		alert("File not uploaded please try again")
		return false;
	}else
		{
		 var allowedFiles = [];
		 var len=fileext.split(",").length;
		for(var sm=0;sm<len;sm++)
			{
					allowedFiles.push(fileext.split(",")[sm]);
			}
		 var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(" + allowedFiles.join('|') + ")$");
		 if (!regex.test(fileUpload.toLowerCase())) {
	    alert("Please upload files having extensions:   " + allowedFiles.join(', ') + "    only.");
	     return false;
		 }else{
		  document.getElementById(inputtxtId).value=fileUpload;
		 }
		 return true;
		}
	
}


