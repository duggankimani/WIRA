package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class ExecuteWorkflowActionHandler extends
		BaseActionHandler<ExecuteWorkflow, ExecuteWorkflowResult> {

	@Inject
	public ExecuteWorkflowActionHandler() {
	}

	@Override
	public ExecuteWorkflowResult execute(ExecuteWorkflow action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {
		
		JBPMHelper.get().execute(action.getTaskId(),action.getUserId(), action.getAction());
		
		ExecuteWorkflowResult result = (ExecuteWorkflowResult)actionResult;
		
		return result;
	}

	@Override
	public void undo(ExecuteWorkflow action, ExecuteWorkflowResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<ExecuteWorkflow> getActionType() {
		return ExecuteWorkflow.class;
	}
}
