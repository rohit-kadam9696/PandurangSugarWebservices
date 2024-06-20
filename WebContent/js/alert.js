  $(document).ready(function(){
	   $("#success-alert").hide();
	    $("#success-alert").fadeTo(2000, 500).slideUp(500, function() {
	  		$("#success-alert").slideUp(500);
	  		});
	  });
  
  function onlynumberallow(textid,msgid,msg)
  {
	  var text=document.getElementById(textid).value;
	  try
	  {
		  if(isNaN(''+text)){
			  document.getElementById(textid).value="";
				document.getElementById(msgid).innerHTML=msg; 
		  }else
			  {
			  document.getElementById(msgid).innerHTML=""; 
			  }
	  }catch(err){
		  document.getElementById(textid).value="";
			document.getElementById(msgid).innerHTML=msg;  
	  }

  }
  
  
  $(":input:not(:disabled)").not($(":button")).keypress(function(evt) {
	     // If the keypress event code is 13 (Enter)
	     if (evt.keyCode == 13) {
	     	  itype = $(this).prop('type');
	     
	         if (itype !== 'submit') {
	             currentTabindex = $(this).attr('tabindex');
	             if (currentTabindex) {
	            	nextInput = $('[tabindex^="'+ (parseInt(currentTabindex)+1) +'"]');
	             	if (nextInput.length) {
	                     nextInput.focus();
	                     nextInput.select();
	                     return false;
	                 }
	             }
	         }
	     }
	 });
  