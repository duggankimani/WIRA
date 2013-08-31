package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class ReloadAttachmentsEvent extends
		GwtEvent<ReloadAttachmentsEvent.ReloadAttachmentsHandler> {

	public static Type<ReloadAttachmentsHandler> TYPE = new Type<ReloadAttachmentsHandler>();

	public interface ReloadAttachmentsHandler extends EventHandler {
		void onReloadAttachments(ReloadAttachmentsEvent event);
	}

	public ReloadAttachmentsEvent() {
	}

	@Override
	protected void dispatch(ReloadAttachmentsHandler handler) {
		handler.onReloadAttachments(this);
	}

	@Override
	public Type<ReloadAttachmentsHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ReloadAttachmentsHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ReloadAttachmentsEvent());
	}
}
