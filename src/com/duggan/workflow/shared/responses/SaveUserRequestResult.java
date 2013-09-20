package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.HTUser;

public class SaveUserRequestResult extends BaseResponse{

	private HTUser user;

	public SaveUserRequestResult() {
		// For serialization only
	}

	public SaveUserRequestResult(HTUser user) {
		this.user = user;
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}
}
