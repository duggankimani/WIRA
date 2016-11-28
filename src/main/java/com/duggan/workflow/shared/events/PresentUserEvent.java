package com.duggan.workflow.shared.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

public class PresentUserEvent extends
		GwtEvent<PresentUserEvent.PresentUserHandler> {

	public static Type<PresentUserHandler> TYPE = new Type<PresentUserHandler>();
	private HTUser user;
	private UserGroup group;
	
	public static Type<PresentUserHandler> getTYPE() {
		return TYPE;
	}

	public UserGroup getGroup() {
		return group;
	}

	public interface PresentUserHandler extends EventHandler {
		void onPresentUser(PresentUserEvent event);
	}

	public PresentUserEvent(HTUser user, UserGroup group) {
		this.user = user;
		this.group = group;
	}

	public HTUser getUser() {
		return user;
	}

	@Override
	protected void dispatch(PresentUserHandler handler) {
		handler.onPresentUser(this);
	}

	@Override
	public Type<PresentUserHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PresentUserHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, HTUser user, UserGroup group) {
		source.fireEvent(new PresentUserEvent(user,group));
	}
}
