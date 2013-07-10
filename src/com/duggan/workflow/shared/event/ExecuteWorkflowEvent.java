package com.duggan.workflow.shared.event;

import com.duggan.workflow.shared.model.Actions;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class ExecuteWorkflowEvent extends
		GwtEvent<ExecuteWorkflowEvent.ExecuteWorkflowHandler> {

	public static Type<ExecuteWorkflowHandler> TYPE = new Type<ExecuteWorkflowHandler>();

	public interface ExecuteWorkflowHandler extends EventHandler {
		void onExecuteWorkflow(ExecuteWorkflowEvent event);
	}

	Actions action;
	
	public ExecuteWorkflowEvent(Actions action) {
		this.action = action;
	}

	@Override
	protected void dispatch(ExecuteWorkflowHandler handler) {
		handler.onExecuteWorkflow(this);
	}

	@Override
	public Type<ExecuteWorkflowHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ExecuteWorkflowHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Actions action) {
		source.fireEvent(new ExecuteWorkflowEvent(action));
	}

	public Actions getAction() {
		return action;
	}

	public void setAction(Actions action) {
		this.action = action;
	}
}
