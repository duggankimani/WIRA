package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.duggan.workflow.client.ui.admin.formbuilder.FormBuilderPresenter;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class DragHandlerImpl implements DragHandler {
	
	private Widget widget;
	
	@Inject FormBuilderPresenter formbuilder;
	
	public DragHandlerImpl(Widget widget){
		this.widget = widget;
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		//System.err.println("DragEnd >>> "+event.getContext().draggable.getClass());
		Widget draggable =event.getContext().draggable;
		System.err.println(event.getContext().draggable);
		if(draggable instanceof Field)
			((Field)draggable).activatePopup();
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
