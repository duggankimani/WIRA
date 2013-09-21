package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class LoadGroupsEvent extends
		GwtEvent<LoadGroupsEvent.LoadGroupsHandler> {

	public static Type<LoadGroupsHandler> TYPE = new Type<LoadGroupsHandler>();

	public interface LoadGroupsHandler extends EventHandler {
		void onLoadGroups(LoadGroupsEvent event);
	}

	public LoadGroupsEvent() {
	}

	@Override
	protected void dispatch(LoadGroupsHandler handler) {
		handler.onLoadGroups(this);
	}

	@Override
	public Type<LoadGroupsHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadGroupsHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new LoadGroupsEvent());
	}
}
