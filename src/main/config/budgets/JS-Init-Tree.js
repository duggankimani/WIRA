
$wnd.jQuery('<link>')
  .appendTo('head')
  .attr({
      type: 'text/css', 
      rel: 'stylesheet',
      href: 'css/jquery.treegrid.css'
  });

$wnd.jQuery.getScript("js/jquery.treegrid.js", function(){
	var rows = $wnd.jQuery('table.budget tr').not(':first');
	rows.each(function(){
		var row = $wnd.jQuery(this);
		if(row.find("input[id='id']")!=null){
			row.addClass("treegrid-"+row.find("input[id='id']").val());
		}
		
		if(row.find("input[id='parent']")!=null){
			row.addClass("treegrid-parent-"+row.find("input[id='parent']").val());
		}
	});

    $wnd.jQuery('.tree').treegrid({
        expanderExpandedClass: 'icon-minus-sign',
            expanderCollapsedClass: 'icon-plus-sign'});
});

