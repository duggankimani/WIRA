package com.duggan.workflow.client.ui.task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class Sorter extends UIObject {

	private static SorterUiBinder uiBinder = GWT.create(SorterUiBinder.class);

	interface SorterUiBinder extends UiBinder<Element, Sorter> {
	}

	public Sorter() {
		setElement(uiBinder.createAndBindUi(this));
		initSort(this.getElement());
	}
	
	private native void initSort(Element element) /*-{
		var that = this;
		$wnd.jQuery($wnd).ready(function(){
			$wnd.jQuery(element).find('a').click(function(){
				//Clear
				
				$wnd.jQuery(element).closest('tr').find('.table-sort a').each(function(){
					$wnd.jQuery(this).removeClass('active');
				});
				
				
				$wnd.jQuery(this).addClass('active');
				
				var i = $wnd.jQuery(this).find('i');
				var dir = 'asc';
				if($wnd.jQuery(i).hasClass('icon-caret-up')){
					dir='asc'
				}else{
					dir='desc';
				}
				
				var id = $wnd.jQuery(element).prop('id');
				that.@com.duggan.workflow.client.ui.task.Sorter::sort(Ljava/lang/String;Ljava/lang/String;)(id,dir);
				
			});
		});
		
	}-*/;
	
	public void sort(String id, String dir){
	}

	public Sorter(String id){
		this();
		this.getElement().setId(id);
	}
	
}
