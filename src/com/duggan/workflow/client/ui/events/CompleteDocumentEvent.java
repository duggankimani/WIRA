package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Boolean;
import com.google.gwt.event.shared.HasHandlers;

public class CompleteDocumentEvent extends
		GwtEvent<CompleteDocumentEvent.CompleteDocumentHandler> {

	public static Type<CompleteDocumentHandler> TYPE = new Type<CompleteDocumentHandler>();
	private Boolean isApproved;
	private Integer documentId;

	public interface CompleteDocumentHandler extends EventHandler {
		void onCompleteDocument(CompleteDocumentEvent event);
	}

	public CompleteDocumentEvent(Integer docId, Boolean isApproved) {
		this.isApproved = isApproved;
		this.documentId = docId;
	}

	public Boolean IsApproved() {
		return isApproved;
	}

	@Override
	protected void dispatch(CompleteDocumentHandler handler) {
		handler.onCompleteDocument(this);
	}

	@Override
	public Type<CompleteDocumentHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CompleteDocumentHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Boolean isApproved, Integer documentId) {
		source.fireEvent(new CompleteDocumentEvent(documentId, isApproved));
	}

	public Integer getDocumentId() {
		return documentId;
	}
}
