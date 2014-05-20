package com.duggan.workflow.client.ui.events;

import com.duggan.workflow.shared.model.Doc;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class WorkflowProcessEvent extends
		GwtEvent<WorkflowProcessEvent.WorkflowProcessHandler> {

	public static Type<WorkflowProcessHandler> TYPE = new Type<WorkflowProcessHandler>();
	private String subject;
	private String action;
	private Doc Document;

	public interface WorkflowProcessHandler extends EventHandler {
		void onWorkflowProcess(WorkflowProcessEvent event);
	}

	public WorkflowProcessEvent(String subject, String action, Doc doc) {
		this.subject=subject;
		this.action = action;
		this.Document=doc;
	}

	public String getAction() {
		return action;
	}

	public Doc getDocument() {
		return Document;
	}
	
	public String getSubject() {
		return subject;
	}

	@Override
	protected void dispatch(WorkflowProcessHandler handler) {
		handler.onWorkflowProcess(this);
	}

	@Override
	public Type<WorkflowProcessHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<WorkflowProcessHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source,String subject, String action, Doc Document) {
		source.fireEvent(new WorkflowProcessEvent(subject,action, Document));
	}
}
