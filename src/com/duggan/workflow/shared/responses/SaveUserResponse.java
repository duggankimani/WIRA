package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.HTUser;

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
