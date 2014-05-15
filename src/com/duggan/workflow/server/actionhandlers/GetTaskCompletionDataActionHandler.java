package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.requests.GetTaskCompletionRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskCompletionResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTaskCompletionDataActionHandler extends
		BaseActionHandler<GetTaskCompletionRequest, GetTaskCompletionResponse> {

	@Inject
	public GetTaskCompletionDataActionHandler() {
	}

	@Override
	public void execute(GetTaskCompletionRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
	
		GetTaskCompletionResponse response = (GetTaskCompletionResponse)actionResult;
		response.setData(DB.getDashboardDao().getTaskCompletionData());
	}
	
	@Override
	public Class<GetTaskCompletionRequest> getActionType() {
		return GetTaskCompletionRequest.class;
	}
}
