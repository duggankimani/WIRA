package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class LoadUsersEvent extends GwtEvent<LoadUsersEvent.LoadUsersHandler> {

	public static Type<LoadUsersHandler> TYPE = new Type<LoadUsersHandler>();
	private Integer start;
	private Integer limit;

	public interface LoadUsersHandler extends EventHandler {
		void onLoadUsers(LoadUsersEvent event);
	}

	public LoadUsersEvent() {
	}

	public LoadUsersEvent(Integer start, Integer limit) {
		this.start = start;
		this.limit = limit;
		
	}

	@Override
	protected void dispatch(LoadUsersHandler handler) {
		handler.onLoadUsers(this);
	}

	@Override
	public Type<LoadUsersHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadUsersHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new LoadUsersEvent());
	}

	public Integer getStart() {
		return start;
	}

	public Integer getLimit() {
		return limit;
	}
}
