package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class LoadProcessesEvent extends
		GwtEvent<LoadProcessesEvent.LoadProcessesHandler> {

	public static Type<LoadProcessesHandler> TYPE = new Type<LoadProcessesHandler>();

	public interface LoadProcessesHandler extends EventHandler {
		void onLoadProcesses(LoadProcessesEvent event);
	}

	public LoadProcessesEvent() {
	}

	@Override
	protected void dispatch(LoadProcessesHandler handler) {
		handler.onLoadProcesses(this);
	}

	@Override
	public Type<LoadProcessesHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadProcessesHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new LoadProcessesEvent());
	}
}
