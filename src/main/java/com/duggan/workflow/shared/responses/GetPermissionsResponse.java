package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.PermissionPOJO;

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
