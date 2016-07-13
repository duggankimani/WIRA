package com.wira.login.shared.request;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;
import com.wira.login.shared.response.ActivateAccountResponse;

public class ActivateAccountRequest extends
		BaseRequest<ActivateAccountResponse> {

	private HTUser user;

	@SuppressWarnings("unused")
	private ActivateAccountRequest() {
		// For serialization only
	}

	public ActivateAccountRequest(HTUser user) {
		this.setUser(user);
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new ActivateAccountResponse();
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}

}
