package com.wira.commons.shared.request;

import com.wira.commons.shared.response.BaseResponse;
import com.wira.commons.shared.response.GetUserRequestResult;

public class GetUserRequest extends BaseRequest<GetUserRequestResult> {

	private String userId;
	private String refId;

	public GetUserRequest() {
		// For serialization only
	}

	public GetUserRequest(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetUserRequestResult();
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}
}
