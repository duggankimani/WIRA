package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.requests.SaveUserRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveUserResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveUserRequestActionHandler extends
		BaseActionHandler<SaveUserRequest, SaveUserResponse> {

	@Inject
	public SaveUserRequestActionHandler() {
	}

	@Override
	public void execute(SaveUserRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		HTUser user = action.getUser();
		
		if(!action.isDelete()){
			user = LoginHelper.get().createUser(user);
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
