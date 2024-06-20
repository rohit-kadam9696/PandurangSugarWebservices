<link rel="stylesheet" href="css/jquery.dataTables.min.css" >
<link rel="stylesheet" href="css/responsive.dataTables.min.css" >
<link rel="stylesheet" href="css/buttons.dataTables.min.css" >

<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.responsive.min.js"></script>
<script src="js/dataTables.buttons.min.js"></script>
<script src="js/buttons.colVis.min.js"></script>

	
<script type="text/javascript" src="js/sweetalert.min.js"> </script>
<script type="text/javascript" src="js/sweetalert.js"> </script>
<script type="text/javascript" src="js/alert.js"> </script>
<script>
$(document).ready(function() {
    $('#example').DataTable( {
        responsive: {
            details: {
                display: $.fn.dataTable.Responsive.display.modal( {
                    header: function ( row ) {
                        var data = row.data();
                        return 'Details for '+data[0]+' '+data[1];
                    }
                } ),
                renderer: $.fn.dataTable.Responsive.renderer.tableAll( {
                    tableClass: 'table'
                } )
            }
        },
        columns: [
            { responsivePriority: 6 },
            { responsivePriority: 5 },
            { responsivePriority: 4 },
            { responsivePriority: 3 },
            { responsivePriority: 2 },
            { responsivePriority: 1 }
        ]
    } );
    
    $('#example1').DataTable( {
    	 responsive: false
    } );
} );
</script>