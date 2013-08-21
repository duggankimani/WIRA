package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.requests.GetProcessStatusRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessStatusRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetProcessStatusRequestActionHandler extends
		BaseActionHandler<GetProcessStatusRequest, GetProcessStatusRequestResult> {

	@Inject
	public GetProcessStatusRequestActionHandler() {
	}

	@Override
	public void execute(GetProcessStatusRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetProcessStatusRequestResult result = (GetProcessStatusRequestResult)actionResult;
		result.setNodes(JBPMHelper.get().getWorkflowProcessDia(action.getProcessInstanceId()));
	}
	
	@Override
	public Class<GetProcessStatusRequest> getActionType() {
		return GetProcessStatusRequest.class;
	}
}
