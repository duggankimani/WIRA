package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.requests.SaveUserRequest;
import com.duggan.workflow.shared.responses.SaveUserResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;

public class SaveUserRequestActionHandler extends
		AbstractActionHandler<SaveUserRequest, SaveUserResponse> {

	@Inject
	public SaveUserRequestActionHandler() {
	}

	@Override
	public void execute(SaveUserRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		HTUser user = action.getUser();
		
		if(!action.isDelete()){
			user = LoginHelper.get().createUser(user, action.isSendActivationEmail());
			SaveUserResponse result = (SaveUserResponse)actionResult;
			result.setUser(user);
		}
		
		
		if(action.isDelete()){
			LoginHelper.get().deleteUser(user);
		}
		
	}

	@Override
	public Class<SaveUserRequest> getActionType() {
		return SaveUserRequest.class;
	}
}
