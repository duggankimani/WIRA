package com.duggan.workflow.client.ui.component.ext;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.Widget;

public class DragHandlerImpl implements DragHandler{
	
	private Widget widget;
	
	public DragHandlerImpl(Widget widget){
		this.widget = widget;
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		
	}

	@Override
	public void onDragStart(DragStartEvent event) {
		
	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
		
	}

}
