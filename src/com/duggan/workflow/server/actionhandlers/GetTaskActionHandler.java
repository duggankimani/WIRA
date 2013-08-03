package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.requests.GetTask;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetTaskResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTaskActionHandler extends
		BaseActionHandler<GetTask, GetTaskResult> {

	@Inject
	public GetTaskActionHandler() {
	}

	@Override
	public void execute(GetTask action, BaseResult actionResult,
			ExecutionContext execContext) throws ActionException {

		HTask task = JBPMHelper.get().getTask(action.getTaskId());
		
		GetTaskResult result = (GetTaskResult)actionResult;
		result.setTask(task);
		
	}

	@Override
	public void undo(GetTask action, GetTaskResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<GetTask> getActionType() {
		return GetTask.class;
	}
}
