package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.Actions;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Long;
import java.util.List;

import com.google.gwt.event.shared.HasHandlers;

public class AfterDocumentLoadEvent extends
		GwtEvent<AfterDocumentLoadEvent.AfterDocumentLoadHandler> {

	public static Type<AfterDocumentLoadHandler> TYPE = new Type<AfterDocumentLoadHandler>();
	private Long documentId;
	private Long taskId;
	private List<Actions> validActions;
	
	public interface AfterDocumentLoadHandler extends EventHandler {
		void onAfterDocumentLoad(AfterDocumentLoadEvent event);
	}

	public AfterDocumentLoadEvent(Long documentId, Long taskId) {
		this.documentId = documentId;
		this.taskId = taskId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	@Override
	protected void dispatch(AfterDocumentLoadHandler handler) {
		handler.onAfterDocumentLoad(this);
	}

	@Override
	public Type<AfterDocumentLoadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AfterDocumentLoadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Long documentId,Long taskId) {
		source.fireEvent(new AfterDocumentLoadEvent(documentId, taskId));
	}

	public List<Actions> getValidActions() {
		return validActions;
	}

	public void setValidActions(List<Actions> validActions) {
		this.validActions = validActions;
	}

	public Long getTaskId() {
		return taskId;
	}
}
