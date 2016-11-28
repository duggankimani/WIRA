package com.duggan.workflow.shared.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AfterAttachmentReloadedEvent extends
		GwtEvent<AfterAttachmentReloadedEvent.AfterAttachmentReloadedHandler> {

	public static Type<AfterAttachmentReloadedHandler> TYPE = new Type<AfterAttachmentReloadedHandler>();
	private String docRefId;

	public interface AfterAttachmentReloadedHandler extends EventHandler {
		void onAfterAttachmentReloaded(AfterAttachmentReloadedEvent event);
	}

	public AfterAttachmentReloadedEvent(String docRefId) {
		this.docRefId = docRefId;
	}

	public String getDocRefId() {
		return docRefId;
	}

	@Override
	protected void dispatch(AfterAttachmentReloadedHandler handler) {
		handler.onAfterAttachmentReloaded(this);
	}

	@Override
	public Type<AfterAttachmentReloadedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AfterAttachmentReloadedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String docRefId) {
		source.fireEvent(new AfterAttachmentReloadedEvent(docRefId));
	}
}
