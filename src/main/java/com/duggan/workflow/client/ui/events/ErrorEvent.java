package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ErrorEvent extends GwtEvent<ErrorEvent.ErrorHandler> {

	public static Type<ErrorHandler> TYPE = new Type<ErrorHandler>();
	private String message;
	private Long id;

	public interface ErrorHandler extends EventHandler {
		void onError(ErrorEvent event);
	}

	public ErrorEvent(String message, Long id) {
		this.message = message;
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public Long getId() {
		return id;
	}

	@Override
	protected void dispatch(ErrorHandler handler) {
		handler.onError(this);
	}

	@Override
	public Type<ErrorHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ErrorHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String message, Long id) {
		source.fireEvent(new ErrorEvent(message, id));
	}
}
