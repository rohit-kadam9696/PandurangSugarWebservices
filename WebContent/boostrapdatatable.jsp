<link rel="stylesheet" href="css/jquery.dataTables.min.css" >
<link rel="stylesheet" href="css/responsive.dataTables.min.css" >
<link rel="stylesheet" href="css/buttons.dataTables.min.css" >	
     
     
<script src="js/boostrapdatatablejs/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="js/boostrapdatatablejs/dataTables.buttons.min.js" type="text/javascript"></script>
<script src="js/boostrapdatatablejs/jszip.min.js" type="text/javascript"></script>
<script src="js/boostrapdatatablejs/pdfmake.min.js" type="text/javascript"></script>
<script src="js/boostrapdatatablejs/vfs_fonts.js" type="text/javascript"></script>
<script src="js/boostrapdatatablejs/buttons.html5.min.js" type="text/javascript"></script>
<script src="js/boostrapdatatablejs/buttons.print.min.js" type="text/javascript"></script>
<script src="js/boostrapdatatablejs/buttons.colVis.min.js" type="text/javascript"></script>

<script>
$(document).ready(function() {
	
    var table=$('#example').DataTable( {
        dom: 'Bfrtip',
        scrollX:true,
        buttons: [
        	  {
                  extend: 'copyHtml5',
                  exportOptions: {
                      columns: [ 0, ':visible' ]
                  }
              },
              {
                  extend: 'excelHtml5',
                  exportOptions: {
                      columns: ':visible'
                  }
              },
              {
                  extend: 'csvHtml5',
                  exportOptions: {
                      columns: ':visible'
                  }
              },
              {
                  extend: 'pdfHtml5',
                  exportOptions: {
                      columns: ':visible'
                  }
              },
              {
                  extend: 'print',
                  exportOptions: {
                      columns: ':visible'
                  }
              },
              {
                  extend: 'colvis',
              }
            ]
    } );
   $('#example1').DataTable();

   var table=$('#report').DataTable( {
	    dom: 'Bfrtip',
	    scrollX:true,
	    buttons: [
	    	  {
	              extend: 'copyHtml5',
	              title: "export table",
	              messageTop: "export table Message",
	              exportOptions: {
	                  columns: [ 0, ':visible' ]
	              }
	          },
	          {
	              extend: 'excelHtml5',
	              title: "export table",
	              messageTop: "export table Message",
	              exportOptions: {
	                  columns: ':visible'
	              }
	          },
	          {
	              extend: 'pdfHtml5',
	              title: '',
	              filename :$("#fileName").html(),
	              messageTop: 'PDF created by Buttons for DataTables. <br> dsakldksal',
	              exportOptions: {
	                  columns: ':visible'
	              }, 
	             
	          },
	          {
	              extend: 'print',
	              title: '',//$("#fileName").html(),
	              messageTop: $("#title").html(),
	              footer: true,
	              filename :$("#fileName").html(),
	              exportOptions: {
	                  columns: ':visible'
	              },
	              customize: function ( win ) {
	                    $(win.document.body)
	                        .css( 'font-size', '10pt' )
	                        /*.prepend(
	                            '<img src="http://datatables.net/media/images/logo-fade.png" style="position:absolute; top:0; left:0;" />'
	                        );*/
	 			   $(win.document.body).find( 'table' )
	                        .addClass( 'compact' )
	                        .css( 'font-size', 'inherit' );
	                }
	          },
	          {
	              extend: 'colvis',
	          }
	        ]
	} );
} );

function hidePrintButton()
{
	$('.buttons-print').hide();
}


</script>

<style>
.text-wrap{
    white-space:normal;
}
</style>

    