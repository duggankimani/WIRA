package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;
import com.wira.login.shared.request.ResetAccountRequest;
import com.wira.login.shared.response.ResetAccountResponse;

public class ResetAccountRequestHandler extends
		AbstractActionHandler<ResetAccountRequest, ResetAccountResponse> {

	@Inject
	public ResetAccountRequestHandler() {
	}

	@Override
	public void execute(ResetAccountRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		UserDaoHelper helper = new UserDaoHelper();
		HTUser userDto = helper.getUserByEmail(action.getEmail());
		if(userDto!=null){
			helper.sendAccountResetEmail(userDto);
		}
		ResetAccountResponse response = (ResetAccountResponse)actionResult;
		response.setUser(userDto);
	}

	@Override
	public Class<ResetAccountRequest> getActionType() {
		return ResetAccountRequest.class;
	}
}
