package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetProcessesRequestActionHandler extends
		AbstractActionHandler<GetProcessesRequest, GetProcessesResponse> {

	@Inject
	public GetProcessesRequestActionHandler() {
	}

	@Override
	public void execute(GetProcessesRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		List<ProcessDef> processes = ProcessDefHelper.getAllProcesses(action.getSearchTerm(),action.isLoadDetails());
		GetProcessesResponse response = (GetProcessesResponse)actionResult;
		
		response.setProcesses(processes);
	}

	@Override
	public Class<GetProcessesRequest> getActionType() {
		return GetProcessesRequest.class;
	}
}
