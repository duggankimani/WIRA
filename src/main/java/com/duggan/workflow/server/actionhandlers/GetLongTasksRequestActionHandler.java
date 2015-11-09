package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.requests.GetLongTasksRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetLongTasksResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetLongTasksRequestActionHandler extends
		AbstractActionHandler<GetLongTasksRequest, GetLongTasksResponse> {

	@Inject
	public GetLongTasksRequestActionHandler() {
	}

	@Override
	public void execute(GetLongTasksRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		GetLongTasksResponse response = (GetLongTasksResponse)actionResult;
		response.setLongTasks(DB.getDashboardDao().getLongLivingTasks());
	}
	
	@Override
	public Class<GetLongTasksRequest> getActionType() {
		return GetLongTasksRequest.class;
	}
}
