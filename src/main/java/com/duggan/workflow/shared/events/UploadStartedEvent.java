package com.duggan.workflow.shared.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UploadStartedEvent extends
		GwtEvent<UploadStartedEvent.UploadStartedHandler> {

	public static Type<UploadStartedHandler> TYPE = new Type<UploadStartedHandler>();

	private Object source;
	
	public interface UploadStartedHandler extends EventHandler {
		void onUploadStarted(UploadStartedEvent event);
	}

	public UploadStartedEvent(Object source) {
		this.source = source;
	}

	@Override
	protected void dispatch(UploadStartedHandler handler) {
		handler.onUploadStarted(this);
	}

	@Override
	public Type<UploadStartedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UploadStartedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new UploadStartedEvent(source));
	}
}
