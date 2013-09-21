package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.duggan.workflow.shared.model.UserGroup;
import com.google.gwt.event.shared.HasHandlers;

public class EditGroupEvent extends GwtEvent<EditGroupEvent.EditGroupHandler> {

	public static Type<EditGroupHandler> TYPE = new Type<EditGroupHandler>();
	private UserGroup group;

	public interface EditGroupHandler extends EventHandler {
		void onEditGroup(EditGroupEvent event);
	}

	public EditGroupEvent(UserGroup group) {
		this.group = group;
	}

	public UserGroup getGroup() {
		return group;
	}

	@Override
	protected void dispatch(EditGroupHandler handler) {
		handler.onEditGroup(this);
	}

	@Override
	public Type<EditGroupHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditGroupHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, UserGroup group) {
		source.fireEvent(new EditGroupEvent(group));
	}
}
