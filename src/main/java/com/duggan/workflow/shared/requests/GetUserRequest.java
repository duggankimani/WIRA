package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetUserRequestResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetUserRequest extends BaseRequest<GetUserRequestResult> {

	private String userId;

	@SuppressWarnings("unused")
	private GetUserRequest() {
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
}
