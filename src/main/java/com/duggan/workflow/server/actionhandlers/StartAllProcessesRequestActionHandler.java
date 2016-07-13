package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.requests.StartAllProcessesRequest;
import com.duggan.workflow.shared.responses.StartAllProcessesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class StartAllProcessesRequestActionHandler extends
		AbstractActionHandler<StartAllProcessesRequest, StartAllProcessesResponse> {

	@Inject
	public StartAllProcessesRequestActionHandler() {
	}

	@Override
	public void execute(StartAllProcessesRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		ProcessMigrationHelper.init();		
	}
	
	@Override
	public Class<StartAllProcessesRequest> getActionType() {
		return StartAllProcessesRequest.class;
	}
}
