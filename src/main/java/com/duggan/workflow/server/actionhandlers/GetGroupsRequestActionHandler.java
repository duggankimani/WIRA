package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetGroupsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetGroupsRequestActionHandler extends
		AbstractActionHandler<GetGroupsRequest, GetGroupsResponse> {

	@Inject
	public GetGroupsRequestActionHandler() {
	}
	
	@Override
	public void execute(GetGroupsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetGroupsResponse response  = (GetGroupsResponse)actionResult;
		
		response.setGroups(LoginHelper.get().getAllGroups());
	}
	
	@Override
	public Class<GetGroupsRequest> getActionType() {
		return GetGroupsRequest.class;
	}
}
