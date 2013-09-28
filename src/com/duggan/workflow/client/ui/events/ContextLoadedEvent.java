package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.duggan.workflow.shared.model.HTUser;
import com.google.gwt.event.shared.HasHandlers;

public class ContextLoadedEvent extends
		GwtEvent<ContextLoadedEvent.ContextLoadedHandler> {

	public static Type<ContextLoadedHandler> TYPE = new Type<ContextLoadedHandler>();
	private HTUser currentUser;

	public interface ContextLoadedHandler extends EventHandler {
		void onContextLoaded(ContextLoadedEvent event);
	}

	public ContextLoadedEvent(HTUser currentUser) {
		this.currentUser = currentUser;
	}

	public HTUser getCurrentUser() {
		return currentUser;
	}

	@Override
	protected void dispatch(ContextLoadedHandler handler) {
		handler.onContextLoaded(this);
	}

	@Override
	public Type<ContextLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ContextLoadedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, HTUser currentUser) {
		source.fireEvent(new ContextLoadedEvent(currentUser));
	}
}
