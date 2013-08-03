package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetNotificationCountResult;

import java.lang.String;

public class GetNotificationCount extends
		BaseRequest<GetNotificationCountResult> {

	private String userId;

	@SuppressWarnings("unused")
	private GetNotificationCount() {
		// For serialization only
	}

	public GetNotificationCount(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
	
	@Override
	public BaseResult createDefaultActionResponse() {
		return new GetNotificationCountResult();
	}
}
