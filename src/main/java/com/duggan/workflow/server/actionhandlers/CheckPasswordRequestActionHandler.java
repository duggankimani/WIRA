package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.shared.requests.CheckPasswordRequest;
import com.duggan.workflow.shared.responses.CheckPasswordRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class CheckPasswordRequestActionHandler extends
		AbstractActionHandler<CheckPasswordRequest, CheckPasswordRequestResult> {

	@Inject
	public CheckPasswordRequestActionHandler() {
	}

	@Override
	public void execute(CheckPasswordRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		String userId = action.getUserId();
		String password = action.getPassword();
		
		boolean isValid = UserDaoHelper.getInstance().login(userId, password);
		CheckPasswordRequestResult result = (CheckPasswordRequestResult)actionResult;

		result.setIsValid(isValid);
		
	}

	@Override
	public Class<CheckPasswordRequest> getActionType() {
		return CheckPasswordRequest.class;
	}
}
