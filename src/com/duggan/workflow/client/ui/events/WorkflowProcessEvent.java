package com.duggan.workflow.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.duggan.workflow.shared.model.DocSummary;
import com.google.gwt.event.shared.HasHandlers;

public class WorkflowProcessEvent extends
		GwtEvent<WorkflowProcessEvent.WorkflowProcessHandler> {

	public static Type<WorkflowProcessHandler> TYPE = new Type<WorkflowProcessHandler>();
	private String action;
	private DocSummary Document;

	public interface WorkflowProcessHandler extends EventHandler {
		void onWorkflowProcess(WorkflowProcessEvent event);
	}

	public WorkflowProcessEvent(String action, DocSummary doc) {
		this.action = action;
		this.Document=doc;
	}

	public String getAction() {
		return action;
	}

	public DocSummary getDocument() {
		return Document;
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

	public static void fire(HasHandlers source, String action, DocSummary Document) {
		source.fireEvent(new WorkflowProcessEvent(action, Document));
	}
}
