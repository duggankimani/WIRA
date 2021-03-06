package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.ProcessLog;
import com.duggan.workflow.shared.requests.GetProcessInstancesRequest;
import com.duggan.workflow.shared.responses.GetProcessInstancesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessInstancesRequestHandler extends 
		AbstractActionHandler<GetProcessInstancesRequest, GetProcessInstancesResponse> {

	@Inject
	public GetProcessInstancesRequestHandler() {
	}

	@Override
	public void execute(GetProcessInstancesRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		((GetProcessInstancesResponse)actionResult).setLogs(
				(ArrayList<ProcessLog>) DB.getProcessDao().getProcessInstances(action.getFilter()));
	}

	@Override
	public Class<GetProcessInstancesRequest> getActionType() {
		return GetProcessInstancesRequest.class;
	}
}
