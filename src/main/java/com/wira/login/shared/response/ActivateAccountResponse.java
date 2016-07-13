package com.wira.login.shared.response;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;

public class ActivateAccountResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HTUser userDto;

	public ActivateAccountResponse() {
		// For serialization only
	}

	public ActivateAccountResponse(HTUser userDto) {
		this.setUser(userDto);
	}

	public HTUser getUser() {
		return userDto;
	}

	public void setUser(HTUser userDto) {
		this.userDto = userDto;
	}
	
}
