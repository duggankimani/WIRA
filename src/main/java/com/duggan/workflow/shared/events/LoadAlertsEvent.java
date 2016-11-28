package com.duggan.workflow.shared.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class LoadAlertsEvent extends
		GwtEvent<LoadAlertsEvent.LoadAlertsHandler> {

	public static Type<LoadAlertsHandler> TYPE = new Type<LoadAlertsHandler>();

	public interface LoadAlertsHandler extends EventHandler {
		void onLoadAlerts(LoadAlertsEvent event);
	}

	public LoadAlertsEvent() {
	}

	@Override
	protected void dispatch(LoadAlertsHandler handler) {
		handler.onLoadAlerts(this);
	}

	@Override
	public Type<LoadAlertsHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadAlertsHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new LoadAlertsEvent());
	}
}
