package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.PermissionPOJO;
import com.wira.commons.shared.request.GetUserRequest;
import com.wira.commons.shared.response.BaseResponse;
import com.wira.commons.shared.response.GetUserRequestResult;

public class GetUserRequestActionHandler extends
		AbstractActionHandler<GetUserRequest, GetUserRequestResult> {

	@Inject
	public GetUserRequestActionHandler() {
	}

	@Override
	public void execute(GetUserRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		HTUser user = null;
		if(action.getUserId()!=null){
			 user= LoginHelper.getHelper().getUser(action.getUserId(), true);
		}else if(action.getRefId()!=null){
			user = new UserDaoHelper().get((User)DB.getUserDao().findByRefId(action.getRefId(), User.class), true);
		}
		
		GetUserRequestResult result = (GetUserRequestResult) actionResult;

		if (user != null) {
			result.setUser(user);
			user.setPermissions((ArrayList<PermissionPOJO>) DB
					.getPermissionDao().getPermissionsForUser(
							user.getUserId()));
		}
	}

	@Override
	public Class<GetUserRequest> getActionType() {
		return GetUserRequest.class;
	}
}
