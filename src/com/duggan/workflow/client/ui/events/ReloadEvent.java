package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class ReloadEvent extends GwtEvent<ReloadEvent.ReloadHandler> {

	public static Type<ReloadHandler> TYPE = new Type<ReloadHandler>();

	public interface ReloadHandler extends EventHandler {
		void onReload(ReloadEvent event);
	}

	public ReloadEvent() {
	}

	@Override
	protected void dispatch(ReloadHandler handler) {
		handler.onReload(this);
	}

	@Override
	public Type<ReloadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ReloadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ReloadEvent());
	}
}
