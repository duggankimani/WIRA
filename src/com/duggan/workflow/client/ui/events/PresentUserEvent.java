package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.UserGroup;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

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
