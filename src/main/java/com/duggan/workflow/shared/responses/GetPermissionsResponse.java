package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.wira.commons.shared.models.PermissionPOJO;
import com.wira.commons.shared.response.BaseResponse;

public class GetPermissionsResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PermissionPOJO> permissions = new ArrayList<PermissionPOJO>();
	
	public GetPermissionsResponse() {
	}

	public ArrayList<PermissionPOJO> getPermissions() {
		return permissions;
	}

	public void setPermissions(ArrayList<PermissionPOJO> permissions) {
		this.permissions = permissions;
	}
}
