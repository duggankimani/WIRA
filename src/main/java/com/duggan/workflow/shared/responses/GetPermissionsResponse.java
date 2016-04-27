package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.PermissionPOJO;

public class GetPermissionsResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<PermissionPOJO> permissions = new ArrayList<PermissionPOJO>();
	
	public GetPermissionsResponse() {
	}

	public List<PermissionPOJO> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionPOJO> permissions) {
		this.permissions = permissions;
	}
}
