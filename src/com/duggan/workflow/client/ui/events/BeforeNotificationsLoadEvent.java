package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class BeforeNotificationsLoadEvent extends
		GwtEvent<BeforeNotificationsLoadEvent.BeforeNotificationsLoadHandler> {

	public static Type<BeforeNotificationsLoadHandler> TYPE = new Type<BeforeNotificationsLoadHandler>();

	public interface BeforeNotificationsLoadHandler extends EventHandler {
		void onBeforeNotificationsLoad(BeforeNotificationsLoadEvent event);
	}

	public BeforeNotificationsLoadEvent() {
	}

	@Override
	protected void dispatch(BeforeNotificationsLoadHandler handler) {
		handler.onBeforeNotificationsLoad(this);
	}

	@Override
	public Type<BeforeNotificationsLoadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<BeforeNotificationsLoadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new BeforeNotificationsLoadEvent());
	}
}
