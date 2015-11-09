package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.requests.AssignTaskRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class AssignTaskActionHandler extends
		AbstractActionHandler<AssignTaskRequest, BaseResponse> {

	@Inject
	public AssignTaskActionHandler() {
	}

	@Override
	public void execute(AssignTaskRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		Long taskId = action.getTaskId();
		String userId = action.getUserId();
		
		JBPMHelper.get().assignTask(taskId, userId);
	}
	
	@Override
	public Class<AssignTaskRequest> getActionType() {
		return AssignTaskRequest.class;
	}
}
