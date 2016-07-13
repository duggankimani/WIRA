package com.wira.commons.shared.response;

import com.wira.commons.shared.models.HTUser;

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
