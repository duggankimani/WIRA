package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.HTUser;

public class GetUserRequestResult extends BaseResponse {

	private HTUser user;

	public GetUserRequestResult() {
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}
}
