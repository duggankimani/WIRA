package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.duggan.workflow.shared.model.DSConfiguration;
import com.google.gwt.event.shared.HasHandlers;

public class EditDSConfigEvent extends
		GwtEvent<EditDSConfigEvent.EditDSConfigHandler> {

	public static Type<EditDSConfigHandler> TYPE = new Type<EditDSConfigHandler>();
	private DSConfiguration configuration;

	public interface EditDSConfigHandler extends EventHandler {
		void onEditDSConfig(EditDSConfigEvent event);
	}

	public EditDSConfigEvent(DSConfiguration configuration) {
		this.configuration = configuration;
	}

	public DSConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	protected void dispatch(EditDSConfigHandler handler) {
		handler.onEditDSConfig(this);
	}

	@Override
	public Type<EditDSConfigHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditDSConfigHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, DSConfiguration configuration) {
		source.fireEvent(new EditDSConfigEvent(configuration));
	}
}
