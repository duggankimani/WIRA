package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;
import com.wira.login.shared.request.ActivateAccountRequest;
import com.wira.login.shared.response.ActivateAccountResponse;

public class ActivateAccountActionHandler extends
		AbstractActionHandler<ActivateAccountRequest, ActivateAccountResponse> {

	@Inject
	public ActivateAccountActionHandler() {
	}

	@Override
	public void execute(ActivateAccountRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		UserDaoHelper helper = new UserDaoHelper();
		HTUser user = action.getUser();
		
		helper.updatePassword(user.getUserId(), user.getPassword());
		ActivateAccountResponse result = (ActivateAccountResponse) actionResult;
		result.setUser(user);
	}

	@Override
	public Class<ActivateAccountRequest> getActionType() {
		return ActivateAccountRequest.class;
	}
}
