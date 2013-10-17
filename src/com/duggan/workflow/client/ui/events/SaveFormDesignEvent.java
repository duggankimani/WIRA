package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class SaveFormDesignEvent extends
		GwtEvent<SaveFormDesignEvent.SaveFormDesignHandler> {

	public static Type<SaveFormDesignHandler> TYPE = new Type<SaveFormDesignHandler>();

	public interface SaveFormDesignHandler extends EventHandler {
		void onSaveFormDesign(SaveFormDesignEvent event);
	}

	public SaveFormDesignEvent() {
	}

	@Override
	protected void dispatch(SaveFormDesignHandler handler) {
		handler.onSaveFormDesign(this);
	}

	@Override
	public Type<SaveFormDesignHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SaveFormDesignHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new SaveFormDesignEvent());
	}
}
