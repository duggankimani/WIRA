package com.duggan.workflow.shared.responses;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;

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
