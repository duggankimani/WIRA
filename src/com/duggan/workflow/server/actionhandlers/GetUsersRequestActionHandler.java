package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetUsersRequestActionHandler extends
		BaseActionHandler<GetUsersRequest, GetUsersResponse> {

	@Inject
	public GetUsersRequestActionHandler() {
	}
	
	@Override
	public void execute(GetUsersRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetUsersResponse response = (GetUsersResponse)actionResult;
		
		response.setUsers(LoginHelper.get().getAllUsers());
	}
	
	@Override
	public Class<GetUsersRequest> getActionType() {
		return GetUsersRequest.class;
	}
}
