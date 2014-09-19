package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.requests.GetTaskStepsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskStepsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTaskStepsRequestHandler extends
		BaseActionHandler<GetTaskStepsRequest, GetTaskStepsResponse> {
	
	@Inject
	public GetTaskStepsRequestHandler() {
	}
	
	@Override
	public void execute(GetTaskStepsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		((GetTaskStepsResponse)actionResult).setSteps(
				ProcessDefHelper.getSteps(action.getProcessId(), action.getNodeId()));
		
	}
	
	public java.lang.Class<GetTaskStepsRequest> getActionType() {
		return GetTaskStepsRequest.class;
	};

}
