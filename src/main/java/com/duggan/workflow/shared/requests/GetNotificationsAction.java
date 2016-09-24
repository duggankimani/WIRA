package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetNotificationsActionResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetNotificationsAction extends
		BaseRequest<GetNotificationsActionResult> {

	private String userId;
	private String processRefId;

	@SuppressWarnings("unused")
	private GetNotificationsAction() {
	}

	public GetNotificationsAction(String userId) {
		this.userId = userId;
	}
	
	public GetNotificationsAction(String processRefId,String userId) {
		this.processRefId = processRefId;
		this.userId = userId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {		
		return new GetNotificationsActionResult();
	}

	public String getUserId() {
		return userId;
	}

	public String getProcessRefId() {
		return processRefId;
	}

}
