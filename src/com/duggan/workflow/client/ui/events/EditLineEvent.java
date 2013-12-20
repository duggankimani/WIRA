package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.duggan.workflow.shared.model.DocumentLine;
import com.google.gwt.event.shared.HasHandlers;

public class EditLineEvent extends GwtEvent<EditLineEvent.EditLineHandler> {

	public static Type<EditLineHandler> TYPE = new Type<EditLineHandler>();
	private DocumentLine line;

	public interface EditLineHandler extends EventHandler {
		void onEditLine(EditLineEvent event);
	}

	public EditLineEvent(DocumentLine line) {
		this.line = line;
	}

	public DocumentLine getLine() {
		return line;
	}

	@Override
	protected void dispatch(EditLineHandler handler) {
		handler.onEditLine(this);
	}

	@Override
	public Type<EditLineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditLineHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, DocumentLine line) {
		source.fireEvent(new EditLineEvent(line));
	}
}
