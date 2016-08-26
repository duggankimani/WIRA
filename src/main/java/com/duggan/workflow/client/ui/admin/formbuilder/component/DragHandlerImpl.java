package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class DragHandlerImpl implements DragHandler {
	
	private Widget widget;
	
	public DragHandlerImpl(Widget widget){
		this.widget = widget;
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		Widget draggable=event.getContext().draggable;
		if(draggable instanceof FieldWidget)
			((FieldWidget)draggable).activatePopup();
	}

	@Override
	public void onDragStart(DragStartEvent event) {
//		System.err.println("Drag start");
	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
//		System.err.println("Preview Drag End");
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
//		System.err.println("Preview Drag Start");
	}

}
