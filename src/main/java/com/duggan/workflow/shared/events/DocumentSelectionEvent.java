package com.duggan.workflow.shared.events;

import com.duggan.workflow.client.ui.util.DocMode;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class DocumentSelectionEvent extends
		GwtEvent<DocumentSelectionEvent.DocumentSelectionHandler> {

	public static Type<DocumentSelectionHandler> TYPE = new Type<DocumentSelectionHandler>();
	private String docRefId;
	private Long taskId;
	private DocMode mode;

	public interface DocumentSelectionHandler extends EventHandler {
		void onDocumentSelection(DocumentSelectionEvent event);
	}

	public DocumentSelectionEvent(String docRefId, Long taskId, DocMode docMode) {
		this.docRefId = docRefId;
		this.taskId = taskId;
	}

	public String getDocRefId() {
		return docRefId;
	}
	
	public DocMode getDocMode(){
		return mode;
	}

	@Override
	protected void dispatch(DocumentSelectionHandler handler) {
		handler.onDocumentSelection(this);
	}

	@Override
	public Type<DocumentSelectionHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DocumentSelectionHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String docRefId, Long taskId, DocMode docMode) {
		source.fireEvent(new DocumentSelectionEvent(docRefId, taskId, docMode));
	}

	public Long getTaskId() {
		return taskId;
	}
}
