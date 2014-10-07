package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.TreeItem;

public class LoadTriggersEvent extends GwtEvent<LoadTriggersEvent.LoadTriggersHandler> {

	public static Type<LoadTriggersHandler> TYPE = new Type<LoadTriggersHandler>();

	public interface LoadTriggersHandler extends EventHandler {
		void onLoadTriggers(LoadTriggersEvent event);
	}

	private TreeItem item;

	public LoadTriggersEvent(TreeItem item) {
		this.item = item;
	}

	@Override
	protected void dispatch(LoadTriggersHandler handler) {
		handler.onLoadTriggers(this);
	}

	@Override
	public Type<LoadTriggersHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadTriggersHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source,TreeItem item) {
		source.fireEvent(new LoadTriggersEvent(item));
	}

	public TreeItem getItem() {
		return item;
	}
}
