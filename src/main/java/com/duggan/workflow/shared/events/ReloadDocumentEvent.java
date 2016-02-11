package com.duggan.workflow.shared.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ReloadDocumentEvent extends
		GwtEvent<ReloadDocumentEvent.ReloadDocumentHandler> {

	public static Type<ReloadDocumentHandler> TYPE = new Type<ReloadDocumentHandler>();
	private String docRefId;

	public interface ReloadDocumentHandler extends EventHandler {
		void onReloadDocument(ReloadDocumentEvent event);
	}

	public ReloadDocumentEvent(String docRefId) {
		this.docRefId = docRefId;
	}

	public String getDocRefId() {
		return docRefId;
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

	public static void fire(HasHandlers source, String docRefId) {
		source.fireEvent(new ReloadDocumentEvent(docRefId));
	}
}
