package com.wira.login.shared.request;

import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;
import com.wira.login.shared.response.ResetAccountResponse;

/**
 * 
 * @author duggan
 *
 */
public class ResetAccountRequest extends BaseRequest<ResetAccountResponse> {

	private String email;
	
	@SuppressWarnings("unused")
	private ResetAccountRequest() {
	}
	
	public ResetAccountRequest(String email){
		this.email = email;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new ResetAccountResponse();
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}

	public String getEmail() {
		return email;
	}
}
