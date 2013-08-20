package com.duggan.workflow.shared.responses;

import java.lang.Boolean;

import com.duggan.workflow.shared.model.HTUser;

public class LoginRequestResult extends BaseResponse {

	private Boolean isValid=false;
	private HTUser user;
	private String sessionId;

	@SuppressWarnings("unused")
	public LoginRequestResult() {
		// For serialization only
	}

	public LoginRequestResult(Boolean isValid) {
		this.isValid = isValid;
	}

	public Boolean isValid() {
		return isValid;
	}
	
	public void setValid(Boolean isValid){
		this.isValid = isValid;
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
