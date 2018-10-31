package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.shared.requests.SaveGroupRequest;
import com.duggan.workflow.shared.responses.SaveGroupResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.UserGroup;
import com.wira.commons.shared.response.BaseResponse;

public class SaveGroupRequestActionHandler extends
		AbstractActionHandler<SaveGroupRequest, SaveGroupResponse> {

	@Inject
	public SaveGroupRequestActionHandler() {
	}
	
	@Override
	public void execute(SaveGroupRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		UserGroup group = action.getGroup();
		
		if(!action.isDelete()){	
			
			group = UserDaoHelper.getInstance().createGroup(group);
			
			//save
			SaveGroupResponse response = (SaveGroupResponse)actionResult;
			response.setGroup(group);
			
		}
		
		if(action.isDelete()){
			UserDaoHelper.getInstance().deleteGroup(group);
		}
	}
	
	@Override
	public Class<SaveGroupRequest> getActionType() {
		return SaveGroupRequest.class;
	}
}
