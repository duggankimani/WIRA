package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;

public class GetUsersRequestActionHandler extends
		AbstractActionHandler<GetUsersRequest, GetUsersResponse> {

	@Inject
	public GetUsersRequestActionHandler() {
	}
	
	@Override
	public void execute(GetUsersRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetUsersResponse response = (GetUsersResponse)actionResult;
		
		response.setUsers((ArrayList<HTUser>) LoginHelper.get().getAllUsers(action.getSearchTerm()));
	}
	
	@Override
	public Class<GetUsersRequest> getActionType() {
		return GetUsersRequest.class;
	}
}
