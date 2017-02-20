/*Borrowed from http://stackoverflow.com/questions/19480719/html-table-selectable-rows-javascript-package
*/
$wnd.jQuery(function() {

    /* Get all rows from your 'table' but not the first one 
     * that includes headers. */
    var rows = $wnd.jQuery('table.budget tr').not(':first');

    /* Create 'click' event handler for rows */
    rows.on('click', function(e) {

        /* Get current row */
        var row = $wnd.jQuery(this);

        /* Check if 'Ctrl', 'cmd' or 'Shift' keyboard key was pressed
         * 'Ctrl' => is represented by 'e.ctrlKey' or 'e.metaKey'
         * 'Shift' => is represented by 'e.shiftKey' */
        if ((e.ctrlKey || e.metaKey) || e.shiftKey) {
            /* If pressed highlight the other row that was clicked */
        	if(row.hasClass('highlight')){
            	row.removeClass('highlight');
            }else{
            	row.addClass('highlight');
            }
            
        	onRowsSelected(rows.find('tr[class=\'hightlight\']'), true);
        } else {
            /* Otherwise just highlight one row and clean others */
        	rows.removeClass('highlight');
        	row.addClass('highlight');
        	
//            if(!row.hasClass('highlight')){
//            	rows.removeClass('highlight');
//            	row.addClass('highlight');
//            }else{
//            	rows.removeClass('highlight');
//            }
            
           onRowSelected(row, row.hasClass('highlight'));
        }

    });

    /* This 'event' is used just to avoid that the table text 
     * gets selected (just for styling). 
     * For example, when pressing 'Shift' keyboard key and clicking 
     * (without this 'event') the text of the 'table' will be selected.
     * You can remove it if you want, I just tested this in 
     * Chrome v30.0.1599.69 */
    $wnd.jQuery($doc).bind('selectstart dragstart', function(e) { 
        e.preventDefault(); return false; 
    });

});

$wnd.jQuery(function() {
	
	var anka = $wnd.jQuery('table.budget').closest('div.budget-module').find('div.actions a');
	anka.click(function(){
		var a = $wnd.jQuery(this);
		var rows = $wnd.jQuery('table.budget').find('tr.highlight');
		
		var id = a.prop("id"); 
		if(id=="aDelete"){
			var ids = new Array();
			rows.each(function(){
				var inputId = $wnd.jQuery(this).find('input[id=\'id\']').val();
				ids.push(inputId);
			});
			
			$wnd.setValue("DeleteRecs",ids);
			alert('Delete -> '+ids);
		}
	});
});

function onRowSelected(row, isSelected){
	var anka = $wnd.jQuery('table.budget').closest('div.budget-module').find('div.actions a');
	if(isSelected){
		anka.removeClass('hide');
	}else{
		anka.addClass('hide');
	}
}

function onRowsSelected(rows, isSelected){
	var anka = $wnd.jQuery('table.budget').closest('div.budget-module').find('div.actions a');
	if(isSelected){
		anka.removeClass('hide');
	}else{
		anka.addClass('hide');
	}
}
