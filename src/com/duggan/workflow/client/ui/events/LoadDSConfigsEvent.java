package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class LoadDSConfigsEvent extends
		GwtEvent<LoadDSConfigsEvent.LoadDSConfigsHandler> {

	public static Type<LoadDSConfigsHandler> TYPE = new Type<LoadDSConfigsHandler>();

	public interface LoadDSConfigsHandler extends EventHandler {
		void onLoadDSConfigs(LoadDSConfigsEvent event);
	}

	public LoadDSConfigsEvent() {
	}

	@Override
	protected void dispatch(LoadDSConfigsHandler handler) {
		handler.onLoadDSConfigs(this);
	}

	@Override
	public Type<LoadDSConfigsHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadDSConfigsHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new LoadDSConfigsEvent());
	}
}
