package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetNotificationsActionResult;

public class GetNotificationsAction extends
		BaseRequest<GetNotificationsActionResult> {

	private String userId;

	@SuppressWarnings("unused")
	private GetNotificationsAction() {
	}

	public GetNotificationsAction(String userId) {
		this.userId = userId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {		
		return new GetNotificationsActionResult();
	}

	public String getUserId() {
		return userId;
	}
}
