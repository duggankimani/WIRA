package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.DocumentLine;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AfterDeleteLineEvent extends
		GwtEvent<AfterDeleteLineEvent.AfterDeleteLineHandler> {

	public static Type<AfterDeleteLineHandler> TYPE = new Type<AfterDeleteLineHandler>();
	private DocumentLine line;

	public interface AfterDeleteLineHandler extends EventHandler {
		void onAfterDeleteLine(AfterDeleteLineEvent event);
	}

	public AfterDeleteLineEvent(DocumentLine line) {
		this.line = line;
	}

	public DocumentLine getLine() {
		return line;
	}

	@Override
	protected void dispatch(AfterDeleteLineHandler handler) {
		handler.onAfterDeleteLine(this);
	}

	@Override
	public Type<AfterDeleteLineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AfterDeleteLineHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, DocumentLine line) {
		source.fireEvent(new AfterDeleteLineEvent(line));
	}
}
