package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.SaveGroupResponse;
import com.wira.commons.shared.models.UserGroup;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveGroupRequest extends BaseRequest<SaveGroupResponse> {

	private UserGroup group;
	private boolean isDelete=false;

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

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
