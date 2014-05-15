package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.requests.UpdatePasswordRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.UpdatePasswordResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class UpdatePasswordRequestActionHandler extends 
		BaseActionHandler<UpdatePasswordRequest, UpdatePasswordResponse> {

	@Inject
	public UpdatePasswordRequestActionHandler() {
	}


	@Override
	public void execute(UpdatePasswordRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		String username = action.getUsername();
		String password = action.getPassword();
		boolean success= LoginHelper.get().updatePassword(username, password);
		
		UpdatePasswordResponse response = (UpdatePasswordResponse)actionResult;
	}

	@Override
	public Class<UpdatePasswordRequest> getActionType() {
		return UpdatePasswordRequest.class;
	}
}
