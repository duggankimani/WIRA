package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ActivitiesSelectedEvent extends
		GwtEvent<ActivitiesSelectedEvent.ActivitiesSelectedHandler> {

	public static Type<ActivitiesSelectedHandler> TYPE = new Type<ActivitiesSelectedHandler>();

	public interface ActivitiesSelectedHandler extends EventHandler {
		void onActivitiesSelected(ActivitiesSelectedEvent event);
	}

	public ActivitiesSelectedEvent() {
	}

	@Override
	protected void dispatch(ActivitiesSelectedHandler handler) {
		handler.onActivitiesSelected(this);
	}

	@Override
	public Type<ActivitiesSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ActivitiesSelectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ActivitiesSelectedEvent());
	}
}
