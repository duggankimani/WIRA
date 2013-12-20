package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.duggan.workflow.shared.model.DocumentLine;
import com.google.gwt.event.shared.HasHandlers;

public class DeleteLineEvent extends
		GwtEvent<DeleteLineEvent.DeleteLineHandler> {

	public static Type<DeleteLineHandler> TYPE = new Type<DeleteLineHandler>();
	private DocumentLine line;

	public interface DeleteLineHandler extends EventHandler {
		void onDeleteLine(DeleteLineEvent event);
	}

	public DeleteLineEvent(DocumentLine line) {
		this.line = line;
	}

	public DocumentLine getLine() {
		return line;
	}

	@Override
	protected void dispatch(DeleteLineHandler handler) {
		handler.onDeleteLine(this);
	}

	@Override
	public Type<DeleteLineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DeleteLineHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, DocumentLine line) {
		source.fireEvent(new DeleteLineEvent(line));
	}
}
