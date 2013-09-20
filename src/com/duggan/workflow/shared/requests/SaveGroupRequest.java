package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveGroupResponse;
import com.duggan.workflow.shared.model.UserGroup;

public class SaveGroupRequest extends BaseRequest<SaveGroupResponse> {

	private UserGroup group;

	@SuppressWarnings("unused")
	private SaveGroupRequest() {
		// For serialization only
	}

	public SaveGroupRequest(UserGroup group) {
		this.group = group;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new SaveGroupResponse();
	}
	
	
}
