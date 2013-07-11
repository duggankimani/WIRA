package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class AfterSaveEvent extends GwtEvent<AfterSaveEvent.AfterSaveHandler> {

	public static Type<AfterSaveHandler> TYPE = new Type<AfterSaveHandler>();

	public interface AfterSaveHandler extends EventHandler {
		void onAfterSave(AfterSaveEvent event);
	}

	public AfterSaveEvent() {
	}

	@Override
	protected void dispatch(AfterSaveHandler handler) {
		handler.onAfterSave(this);
	}

	@Override
	public Type<AfterSaveHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AfterSaveHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new AfterSaveEvent());
	}
}
