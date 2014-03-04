package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

public class ClientDisconnectionEvent extends
		GwtEvent<ClientDisconnectionEvent.ClientDisconnectionHandler> {

	public static Type<ClientDisconnectionHandler> TYPE = new Type<ClientDisconnectionHandler>();
	private String message;

	public interface ClientDisconnectionHandler extends EventHandler {
		void onClientDisconnection(ClientDisconnectionEvent event);
	}

	public ClientDisconnectionEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	protected void dispatch(ClientDisconnectionHandler handler) {
		handler.onClientDisconnection(this);
	}

	@Override
	public Type<ClientDisconnectionHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ClientDisconnectionHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String message) {
		source.fireEvent(new ClientDisconnectionEvent(message));
	}
}
