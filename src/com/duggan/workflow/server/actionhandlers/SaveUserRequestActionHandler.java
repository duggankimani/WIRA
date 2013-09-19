package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.requests.SaveUserRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveUserRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveUserRequestActionHandler extends
		BaseActionHandler<SaveUserRequest, SaveUserRequestResult> {

	@Inject
	public SaveUserRequestActionHandler() {
	}

	@Override
	public void execute(SaveUserRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		HTUser user = action.getUser();
		
	}

	@Override
	public Class<SaveUserRequest> getActionType() {
		return SaveUserRequest.class;
	}
}
