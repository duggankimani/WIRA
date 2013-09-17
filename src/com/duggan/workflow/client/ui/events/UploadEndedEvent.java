package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class UploadEndedEvent extends
		GwtEvent<UploadEndedEvent.UploadEndedHandler> {

	public static Type<UploadEndedHandler> TYPE = new Type<UploadEndedHandler>();

	public interface UploadEndedHandler extends EventHandler {
		void onUploadEnded(UploadEndedEvent event);
	}

	public UploadEndedEvent() {
	}

	@Override
	protected void dispatch(UploadEndedHandler handler) {
		handler.onUploadEnded(this);
	}

	@Override
	public Type<UploadEndedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UploadEndedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new UploadEndedEvent());
	}
}
