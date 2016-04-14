package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.requests.GetUserRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetUserRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetUserRequestActionHandler extends
		AbstractActionHandler<GetUserRequest, GetUserRequestResult> {

	@Inject
	public GetUserRequestActionHandler() {
	}
	
	@Override
	public void execute(GetUserRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		HTUser user = LoginHelper.getHelper().getUser(action.getUserId(), true);
		GetUserRequestResult result = (GetUserRequestResult)actionResult;
	
		result.setUser(user);
		user.setPermissions(DB.getPermissionDao().getPermissionsForUser(action.getUserId()));
	}
	
	
	@Override
	public Class<GetUserRequest> getActionType() {
		return GetUserRequest.class;
	}
}
