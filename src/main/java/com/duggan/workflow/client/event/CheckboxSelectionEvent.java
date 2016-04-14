package com.duggan.workflow.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CheckboxSelectionEvent extends
		GwtEvent<CheckboxSelectionEvent.CheckboxSelectionHandler> {
	private static Type<CheckboxSelectionHandler> TYPE = new Type<CheckboxSelectionHandler>();

	public interface CheckboxSelectionHandler extends EventHandler {
		void onCheckboxSelection(CheckboxSelectionEvent event);
	}

	private Object model;
	private boolean value;

	public CheckboxSelectionEvent(final Object model, boolean value) {
		this.model = model;
		this.value = value;
	}

	public static Type<CheckboxSelectionHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final CheckboxSelectionHandler handler) {
		handler.onCheckboxSelection(this);
	}

	@Override
	public Type<CheckboxSelectionHandler> getAssociatedType() {
		return TYPE;
	}

	public Object getModel() {
		return model;
	}

	public boolean getValue() {
		return value;
	}
	
}
