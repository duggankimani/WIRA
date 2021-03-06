package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.DocumentType;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CreateDocumentEvent extends
		GwtEvent<CreateDocumentEvent.CreateDocumentHandler> {

	public static Type<CreateDocumentHandler> TYPE = new Type<CreateDocumentHandler>();
	private DocumentType docType;
	private String processRefId;

	public interface CreateDocumentHandler extends EventHandler {
		void onCreateDocument(CreateDocumentEvent event);
	}

	@Deprecated
	public CreateDocumentEvent(DocumentType docType) {
		this.docType = docType;
	}
	
	public CreateDocumentEvent(String processRefId) {
		this.processRefId = processRefId;
	}

	public DocumentType getDocType() {
		return docType;
	}

	@Override
	protected void dispatch(CreateDocumentHandler handler) {
		handler.onCreateDocument(this);
	}

	@Override
	public Type<CreateDocumentHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CreateDocumentHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String processRefId) {
		source.fireEvent(new CreateDocumentEvent(processRefId));
	}
	
	public String getProcessRefId() {
		return processRefId;
	}

}
