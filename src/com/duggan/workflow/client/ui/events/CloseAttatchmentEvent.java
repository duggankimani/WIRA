package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class CloseAttatchmentEvent extends
		GwtEvent<CloseAttatchmentEvent.CloseAttatchmentHandler> {

	public static Type<CloseAttatchmentHandler> TYPE = new Type<CloseAttatchmentHandler>();

	public interface CloseAttatchmentHandler extends EventHandler {
		void onCloseAttatchment(CloseAttatchmentEvent event);
	}

	public CloseAttatchmentEvent() {
	}

	@Override
	protected void dispatch(CloseAttatchmentHandler handler) {
		handler.onCloseAttatchment(this);
	}

	@Override
	public Type<CloseAttatchmentHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CloseAttatchmentHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CloseAttatchmentEvent());
	}
}
