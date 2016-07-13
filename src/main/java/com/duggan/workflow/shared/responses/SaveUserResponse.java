package com.duggan.workflow.shared.responses;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;

public class SaveUserResponse extends BaseResponse{

	private HTUser user;

	public SaveUserResponse() {
		// For serialization only
	}

	public SaveUserResponse(HTUser user) {
		this.user = user;
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}
}
