<script type="text/javascript">
$(function() {
  $('.datetimepicker').datetimepicker({
    language: 'en',
    pick12HourFormat: true
  }).on('changeDate', function(ev){
	     $(this).datetimepicker('hide');
	 });
  
  $('#datetimepicker3').datetimepicker({
	    language: 'en',
	    pick12HourFormat: true
	  });
  $('#date1').datetimepicker({
	    language: 'en',
	    pick12HourFormat: true
	  }).on('changeDate', function(ev){
		     
		     $('#date1').datetimepicker('hide');
		 });
 
  $('#fromdate').datetimepicker({
	    language: 'en',
	    pick12HourFormat: true
	  }).on('changeDate', function(ev){
		     
		     $('#date1').datetimepicker('hide');
		 });
  
  
  $('#todate').datetimepicker({
	    language: 'en',
	    pick12HourFormat: true
	  }).on('changeDate', function(ev){
		     
		     $('#date1').datetimepicker('hide');
		 });
  
});
</script> 

