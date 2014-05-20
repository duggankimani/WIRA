package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.UserGroup;

public class SaveGroupResponse extends BaseResponse {

	private UserGroup group;

	public SaveGroupResponse() {
		// For serialization only
	}

	public SaveGroupResponse(UserGroup group) {
		this.group = group;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}
}
