package com.duggan.workflow.client.ui.events;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Actions;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AfterDocumentLoadEvent extends
		GwtEvent<AfterDocumentLoadEvent.AfterDocumentLoadHandler> {

	public static Type<AfterDocumentLoadHandler> TYPE = new Type<AfterDocumentLoadHandler>();
	private String docRefId;
	private Long taskId;
	private ArrayList<Actions> validActions;
	
	public interface AfterDocumentLoadHandler extends EventHandler {
		void onAfterDocumentLoad(AfterDocumentLoadEvent event);
	}

	public AfterDocumentLoadEvent(String docRefId, Long taskId) {
		this.docRefId = docRefId;
		this.taskId = taskId;
	}

	public String getDocRefId() {
		return docRefId;
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

	public static void fire(HasHandlers source, String docRefId,Long taskId) {
		source.fireEvent(new AfterDocumentLoadEvent(docRefId, taskId));
	}

	public ArrayList<Actions> getValidActions() {
		return validActions;
	}

	public void setValidActions(ArrayList<Actions> validActions) {
		this.validActions = validActions;
	}

	public Long getTaskId() {
		return taskId;
	}
}
