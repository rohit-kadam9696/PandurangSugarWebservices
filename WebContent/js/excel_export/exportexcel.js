 function exportexcel(filename) {  
	 
	alert("filename"+filename)
                         	var d = new Date();
                      		var month = d.getMonth()+1;
                      		var day = d.getDate();
                      		var output = 
                      		    ((''+day).length<2 ? '0' : '') + day  + '_' +  ((''+month).length<2 ? '0' : '') + month+ '_' + d.getFullYear();
                      		 $("#printTable").table2excel({  
                                 name: "Table2Excel",  
                                 filename: filename+"_"+output,  
                                 fileext: ".xls"  
                             });  
                         }