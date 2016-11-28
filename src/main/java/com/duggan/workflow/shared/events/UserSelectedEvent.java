package com.duggan.workflow.shared.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.commons.shared.models.HTUser;

public class UserSelectedEvent extends
		GwtEvent<UserSelectedEvent.UserSelectedHandler> {

	public static Type<UserSelectedHandler> TYPE = new Type<UserSelectedHandler>();
	private HTUser user;

	public interface UserSelectedHandler extends EventHandler {
		void onUserSelected(UserSelectedEvent event);
	}

	public UserSelectedEvent(HTUser user) {
		this.user = user;
	}

	public HTUser getUser() {
		return user;
	}

	@Override
	protected void dispatch(UserSelectedHandler handler) {
		handler.onUserSelected(this);
	}

	@Override
	public Type<UserSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserSelectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, HTUser user) {
		source.fireEvent(new UserSelectedEvent(user));
	}
}
