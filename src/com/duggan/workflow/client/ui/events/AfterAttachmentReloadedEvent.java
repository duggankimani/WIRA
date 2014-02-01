package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;
import com.google.gwt.event.shared.HasHandlers;

public class AfterAttachmentReloadedEvent extends
		GwtEvent<AfterAttachmentReloadedEvent.AfterAttachmentReloadedHandler> {

	public static Type<AfterAttachmentReloadedHandler> TYPE = new Type<AfterAttachmentReloadedHandler>();
	private Long documentId;

	public interface AfterAttachmentReloadedHandler extends EventHandler {
		void onAfterAttachmentReloaded(AfterAttachmentReloadedEvent event);
	}

	public AfterAttachmentReloadedEvent(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
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

	public static void fire(HasHandlers source, Long documentId) {
		source.fireEvent(new AfterAttachmentReloadedEvent(documentId));
	}
}
