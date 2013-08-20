package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveNotificationRequestResult;

import java.lang.Long;
import java.lang.Boolean;

public class SaveNotificationRequest extends
		BaseRequest<SaveNotificationRequestResult> {

	private Long notificationId;
	private Boolean read;

	@SuppressWarnings("unused")
	private SaveNotificationRequest() {
		// For serialization only
	}

	public SaveNotificationRequest(Long notificationId, Boolean read) {
		this.notificationId = notificationId;
		this.read = read;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveNotificationRequestResult();
	}
	
	public Long getNotificationId() {
		return notificationId;
	}

	public Boolean getRead() {
		return read;
	}
}
