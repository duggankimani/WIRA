package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetPermissionsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetPermissionsRequest extends BaseRequest<GetPermissionsResponse> {
	
	private String username;
	private String roleName;
	
	public GetPermissionsRequest() {
	}
	
	public GetPermissionsRequest(String username, String roleName) {
		this.username = username;
		this.roleName = roleName;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetPermissionsResponse();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
