package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;

public class DeleteNotificationTemplateRequest extends
		BaseRequest<BaseResponse> {

	private Long notificationId;
	
	@SuppressWarnings("unused")
	private DeleteNotificationTemplateRequest() {
		// For serialization only
	}

	public DeleteNotificationTemplateRequest(Long notificationId) {
		this.notificationId = notificationId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new BaseResponse();
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

}
