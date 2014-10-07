package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.Trigger;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.TreeItem;

public class EditTriggerEvent extends
		GwtEvent<EditTriggerEvent.EditTriggerHandler> {

	public static Type<EditTriggerHandler> TYPE = new Type<EditTriggerHandler>();
	private Trigger trigger;
	private TreeItem item;

	public interface EditTriggerHandler extends EventHandler {
		void onEditTrigger(EditTriggerEvent event);
	}

	public EditTriggerEvent(Trigger trigger) {
		this.trigger = trigger;
	}
	
	public EditTriggerEvent(TreeItem item,Trigger trigger) {
		this.trigger = trigger;
		this.item=item;
	}
	
	@Override
	protected void dispatch(EditTriggerHandler handler) {
		handler.onEditTrigger(this);
	}

	@Override
	public Type<EditTriggerHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditTriggerHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Trigger trigger) {
		source.fireEvent(new EditTriggerEvent(trigger));
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public TreeItem getItem() {
		return item;
	}
}
