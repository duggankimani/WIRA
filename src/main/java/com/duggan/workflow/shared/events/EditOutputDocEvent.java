package com.duggan.workflow.shared.events;

import com.duggan.workflow.shared.model.OutputDocument;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class EditOutputDocEvent extends
		GwtEvent<EditOutputDocEvent.EditOutputDocHandler> {

	public static Type<EditOutputDocHandler> TYPE = new Type<EditOutputDocHandler>();
	private OutputDocument doc;

	public interface EditOutputDocHandler extends EventHandler {
		void onEditOutputDoc(EditOutputDocEvent event);
	}

	public EditOutputDocEvent(OutputDocument doc) {
		this.doc = doc;
	}

	@Override
	protected void dispatch(EditOutputDocHandler handler) {
		handler.onEditOutputDoc(this);
	}

	@Override
	public Type<EditOutputDocHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditOutputDocHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, OutputDocument doc) {
		source.fireEvent(new EditOutputDocEvent(doc));
	}

	public OutputDocument getDoc() {
		return doc;
	}
}
