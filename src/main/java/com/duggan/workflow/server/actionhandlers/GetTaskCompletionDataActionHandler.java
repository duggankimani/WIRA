package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.dashboard.Data;
import com.duggan.workflow.shared.requests.GetTaskCompletionRequest;
import com.duggan.workflow.shared.responses.GetTaskCompletionResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskCompletionDataActionHandler extends
		AbstractActionHandler<GetTaskCompletionRequest, GetTaskCompletionResponse> {

	@Inject
	public GetTaskCompletionDataActionHandler() {
	}

	@Override
	public void execute(GetTaskCompletionRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
	
		GetTaskCompletionResponse response = (GetTaskCompletionResponse)actionResult;
		response.setData((ArrayList<Data>) DB.getDashboardDao().getTaskCompletionData());
	}
	
	@Override
	public Class<GetTaskCompletionRequest> getActionType() {
		return GetTaskCompletionRequest.class;
	}
}
