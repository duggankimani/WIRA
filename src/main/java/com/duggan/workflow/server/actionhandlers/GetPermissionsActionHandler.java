package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.PermissionDao;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.requests.GetPermissionsRequest;
import com.duggan.workflow.shared.responses.GetPermissionsResponse;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.PermissionPOJO;
import com.wira.commons.shared.response.BaseResponse;

public class GetPermissionsActionHandler extends
		AbstractActionHandler<GetPermissionsRequest, GetPermissionsResponse> {

	public GetPermissionsActionHandler() {
	}

	@Override
	public void execute(GetPermissionsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		PermissionDao permissionDao = DB.getPermissionDao();
		GetPermissionsResponse aResponse = (GetPermissionsResponse) actionResult;

		if (action.getUsername() != null) {
			aResponse.setPermissions((ArrayList<PermissionPOJO>) permissionDao.getPermissionsForUser(action
					.getUsername()));
		} else if (action.getRoleName() != null) {
			aResponse.setPermissions((ArrayList<PermissionPOJO>) permissionDao.getPermissionsForRole(action
					.getRoleName()));
		} else {
			aResponse.setPermissions((ArrayList<PermissionPOJO>) permissionDao.getAllPermissions(0, 100));
		}
	}

	@Override
	public Class<GetPermissionsRequest> getActionType() {
		return GetPermissionsRequest.class;
	}

}
