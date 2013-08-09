package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class ProcessingEvent extends
		GwtEvent<ProcessingEvent.ProcessingHandler> {

	public static Type<ProcessingHandler> TYPE = new Type<ProcessingHandler>();

	public interface ProcessingHandler extends EventHandler {
		void onProcessing(ProcessingEvent event);
	}

	public ProcessingEvent() {
	}

	@Override
	protected void dispatch(ProcessingHandler handler) {
		handler.onProcessing(this);
	}

	@Override
	public Type<ProcessingHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ProcessingHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ProcessingEvent());
	}
}
