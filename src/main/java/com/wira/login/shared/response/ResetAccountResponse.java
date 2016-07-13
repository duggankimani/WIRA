package com.wira.login.shared.response;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;

public class ResetAccountResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HTUser user;

	public ResetAccountResponse() {
		// For serialization only
	}

	public ResetAccountResponse(HTUser user) {
		this.user = user;
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}
}
