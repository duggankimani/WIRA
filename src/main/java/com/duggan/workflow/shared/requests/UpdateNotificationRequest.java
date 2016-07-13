package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.UpdateNotificationRequestResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class UpdateNotificationRequest extends
		BaseRequest<UpdateNotificationRequestResult> {

	private Long notificationId;
	private Boolean read;

	@SuppressWarnings("unused")
	private UpdateNotificationRequest() {
		// For serialization only
	}

	public UpdateNotificationRequest(Long notificationId, Boolean read) {
		this.notificationId = notificationId;
		this.read = read;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new UpdateNotificationRequestResult();
	}
	
	public Long getNotificationId() {
		return notificationId;
	}

	public Boolean getRead() {
		return read;
	}
}
