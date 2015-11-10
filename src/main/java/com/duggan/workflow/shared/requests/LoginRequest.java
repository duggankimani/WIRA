package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.ActionType;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.LoginRequestResult;

public class LoginRequest extends BaseRequest<LoginRequestResult> {

	private String username;
	private String password;
	private ActionType actionType;
	private String loggedInCookie;
	
	@SuppressWarnings("unused")
	private LoginRequest() {
		// For serialization only
	}

	public LoginRequest(String username, String password) {
		actionType = ActionType.VIA_CREDENTIALS;
		this.username = username;
		this.password = password;
	}
	
	public LoginRequest(String loggedInCookie){
		actionType = ActionType.VIA_COOKIE;
		this.loggedInCookie = loggedInCookie;
	}

	public String getLoggedInCookie() {
		return loggedInCookie;
	}

	public ActionType getActionType() {
		return actionType;
	}

	@Override
	public boolean isSecured() {
		return false;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {

		return new LoginRequestResult();
	}
}
