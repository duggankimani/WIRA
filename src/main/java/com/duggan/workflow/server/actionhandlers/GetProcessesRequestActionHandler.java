package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.responses.GetProcessesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessesRequestActionHandler extends
		AbstractActionHandler<GetProcessesRequest, GetProcessesResponse> {

	@Inject
	public GetProcessesRequestActionHandler() {
	}

	@Override
	public void execute(GetProcessesRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		List<ProcessDef> processes = ProcessDaoHelper.getAllProcesses(action.getSearchTerm(),action.isLoadDetails());
		GetProcessesResponse response = (GetProcessesResponse)actionResult;
		
		response.setProcesses((ArrayList<ProcessDef>) processes);
	}

	@Override
	public Class<GetProcessesRequest> getActionType() {
		return GetProcessesRequest.class;
	}
}
