package com.duggan.workflow.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EditModelEvent extends GwtEvent<EditModelEvent.EditModelHandler> {
	private static Type<EditModelHandler> TYPE = new Type<EditModelHandler>();

	public interface EditModelHandler extends EventHandler {
		void onEditModel(EditModelEvent event);
	}

	private Object model;

	public EditModelEvent(final Object model) {
		this.model = model;

	}

	public static Type<EditModelHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final EditModelHandler handler) {
		handler.onEditModel(this);
	}

	@Override
	public Type<EditModelHandler> getAssociatedType() {
		return TYPE;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

}
