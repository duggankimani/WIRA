package com.duggan.workflow.server.actionhandlers;

import java.util.Arrays;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetGroupsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetGroupsRequestActionHandler extends
		AbstractActionHandler<GetGroupsRequest, GetGroupsResponse> {

	@Inject
	public GetGroupsRequestActionHandler() {
	}
	
	@Override
	public void execute(GetGroupsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetGroupsResponse response  = (GetGroupsResponse)actionResult;
		
		if(action.getGroupName()!=null){
			UserGroup group = LoginHelper.get().getGroupById(action.getGroupName());
			group.setPermissions(DB.getPermissionDao().getPermissionsForRole(action.getGroupName()));
			response.setGroups(Arrays.asList(group));
		}else{
			response.setGroups(LoginHelper.get().getAllGroups(action.getSearchTerm()));
		}
		
	}
	
	@Override
	public Class<GetGroupsRequest> getActionType() {
		return GetGroupsRequest.class;
	}
}
