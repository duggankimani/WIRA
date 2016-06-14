package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.TaskLog;
import com.duggan.workflow.shared.requests.GetProcessLogRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessLogResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetProcessLogRequestHandler extends 
		AbstractActionHandler<GetProcessLogRequest, GetProcessLogResponse> {

	@Inject
	public GetProcessLogRequestHandler() {
	}

	@Override
	public void execute(GetProcessLogRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		((GetProcessLogResponse)actionResult).setLogs(
				(ArrayList<TaskLog>) ProcessDefHelper.getProcessLog(action.getProcessInstanceId(), "en-UK"));
	}

	@Override
	public void undo(GetProcessLogRequest action, GetProcessLogResponse result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetProcessLogRequest> getActionType() {
		return GetProcessLogRequest.class;
	}
}
