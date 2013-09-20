package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.requests.SaveGroupRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveGroupResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveGroupRequestActionHandler extends
		BaseActionHandler<SaveGroupRequest, SaveGroupResponse> {

	@Inject
	public SaveGroupRequestActionHandler() {
	}
	
	@Override
	public void execute(SaveGroupRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		UserGroup group = LoginHelper.get().createGroup(action.getGroup());
		
		SaveGroupResponse response = (SaveGroupResponse)actionResult;
		response.setGroup(group);
	}
	
	@Override
	public Class<SaveGroupRequest> getActionType() {
		return SaveGroupRequest.class;
	}
}
