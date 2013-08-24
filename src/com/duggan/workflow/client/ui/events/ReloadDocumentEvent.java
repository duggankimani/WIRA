package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;
import com.google.gwt.event.shared.HasHandlers;

public class ReloadDocumentEvent extends
		GwtEvent<ReloadDocumentEvent.ReloadDocumentHandler> {

	public static Type<ReloadDocumentHandler> TYPE = new Type<ReloadDocumentHandler>();
	private Long documentId;

	public interface ReloadDocumentHandler extends EventHandler {
		void onReloadDocument(ReloadDocumentEvent event);
	}

	public ReloadDocumentEvent(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	@Override
	protected void dispatch(ReloadDocumentHandler handler) {
		handler.onReloadDocument(this);
	}

	@Override
	public Type<ReloadDocumentHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ReloadDocumentHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long documentId) {
		source.fireEvent(new ReloadDocumentEvent(documentId));
	}
}
