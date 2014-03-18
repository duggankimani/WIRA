package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.UpdatePasswordResponse;

import java.lang.String;

public class UpdatePasswordRequest extends
		BaseRequest<UpdatePasswordResponse> {

	private String username;
	private String password;

	@SuppressWarnings("unused")
	private UpdatePasswordRequest() {
		// For serialization only
	}

	public UpdatePasswordRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new UpdatePasswordResponse();
	}
}
