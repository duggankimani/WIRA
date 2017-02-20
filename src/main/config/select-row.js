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
            row.addClass('highlight');
        } else {
            /* Otherwise just highlight one row and clean others */
            rows.removeClass('highlight');
            row.addClass('highlight');
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
