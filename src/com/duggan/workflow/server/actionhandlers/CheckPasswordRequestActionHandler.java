package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.requests.CheckPasswordRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.CheckPasswordRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class CheckPasswordRequestActionHandler extends
		BaseActionHandler<CheckPasswordRequest, CheckPasswordRequestResult> {

	@Inject
	public CheckPasswordRequestActionHandler() {
	}

	@Override
	public void execute(CheckPasswordRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		String userId = action.getUserId();
		String password = action.getPassword();
		
		boolean isValid = LoginHelper.get().login(userId, password);
		CheckPasswordRequestResult result = (CheckPasswordRequestResult)actionResult;

		result.setIsValid(isValid);
		
	}

	@Override
	public Class<CheckPasswordRequest> getActionType() {
		return CheckPasswordRequest.class;
	}
}
