package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.PermissionDao;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.requests.GetPermissionsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetPermissionsResponse;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

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
			aResponse.setPermissions(permissionDao.getPermissionsForUser(action
					.getUsername()));
		} else if (action.getRoleName() != null) {
			aResponse.setPermissions(permissionDao.getPermissionsForRole(action
					.getRoleName()));
		} else {
			aResponse.setPermissions(permissionDao.getAllPermissions(0, 100));
		}
	}

	@Override
	public Class<GetPermissionsRequest> getActionType() {
		return GetPermissionsRequest.class;
	}

}
