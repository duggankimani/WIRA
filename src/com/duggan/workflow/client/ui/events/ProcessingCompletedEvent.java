package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class ProcessingCompletedEvent extends
		GwtEvent<ProcessingCompletedEvent.ProcessingCompletedHandler> {

	public static Type<ProcessingCompletedHandler> TYPE = new Type<ProcessingCompletedHandler>();

	public interface ProcessingCompletedHandler extends EventHandler {
		void onProcessingCompleted(ProcessingCompletedEvent event);
	}

	public ProcessingCompletedEvent() {
	}

	@Override
	protected void dispatch(ProcessingCompletedHandler handler) {
		handler.onProcessingCompleted(this);
	}

	@Override
	public Type<ProcessingCompletedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ProcessingCompletedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ProcessingCompletedEvent());
	}
}
