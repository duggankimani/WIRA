package com.duggan.workflow.shared.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class LoadFormBuilderEvent extends
		GwtEvent<LoadFormBuilderEvent.LoadFormBuilderHandler> {

	public static Type<LoadFormBuilderHandler> TYPE = new Type<LoadFormBuilderHandler>();

	public interface LoadFormBuilderHandler extends EventHandler {
		void onLoadFormBuilder(LoadFormBuilderEvent event);
	}

	public interface LoadFormBuilderHasHandlers extends HasHandlers {
		HandlerRegistration addLoadFormBuilderHandler(LoadFormBuilderHandler handler);
	}

	public LoadFormBuilderEvent() {
	}

	@Override
	protected void dispatch(LoadFormBuilderHandler handler) {
		handler.onLoadFormBuilder(this);
	}

	@Override
	public Type<LoadFormBuilderHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadFormBuilderHandler> getType() {
		return TYPE;
	}
}
