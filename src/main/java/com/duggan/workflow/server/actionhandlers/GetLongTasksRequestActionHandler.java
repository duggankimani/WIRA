package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.duggan.workflow.shared.requests.GetLongTasksRequest;
import com.duggan.workflow.shared.responses.GetLongTasksResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetLongTasksRequestActionHandler extends
		AbstractActionHandler<GetLongTasksRequest, GetLongTasksResponse> {

	@Inject
	public GetLongTasksRequestActionHandler() {
	}

	@Override
	public void execute(GetLongTasksRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		GetLongTasksResponse response = (GetLongTasksResponse)actionResult;
		response.setLongTasks((ArrayList<LongTask>) DB.getDashboardDao().getLongLivingTasks());
	}
	
	@Override
	public Class<GetLongTasksRequest> getActionType() {
		return GetLongTasksRequest.class;
	}
}
