package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class LoadOrganizationsEvent extends
		GwtEvent<LoadOrganizationsEvent.LoadOrganizationsHandler> {

	public static Type<LoadOrganizationsHandler> TYPE = new Type<LoadOrganizationsHandler>();

	public interface LoadOrganizationsHandler extends EventHandler {
		void onLoadOrganizations(LoadOrganizationsEvent event);
	}

	public LoadOrganizationsEvent() {
	}

	@Override
	protected void dispatch(LoadOrganizationsHandler handler) {
		handler.onLoadOrganizations(this);
	}

	@Override
	public Type<LoadOrganizationsHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadOrganizationsHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new LoadOrganizationsEvent());
	}
}
