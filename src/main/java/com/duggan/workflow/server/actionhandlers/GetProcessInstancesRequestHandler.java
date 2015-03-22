package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.requests.GetProcessInstancesRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessInstancesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetProcessInstancesRequestHandler extends 
		BaseActionHandler<GetProcessInstancesRequest, GetProcessInstancesResponse> {

	@Inject
	public GetProcessInstancesRequestHandler() {
	}

	@Override
	public void execute(GetProcessInstancesRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		((GetProcessInstancesResponse)actionResult).setLogs(
				DB.getProcessDao().getProcessInstances(action.getFilter()));
	}

	@Override
	public Class<GetProcessInstancesRequest> getActionType() {
		return GetProcessInstancesRequest.class;
	}
}
