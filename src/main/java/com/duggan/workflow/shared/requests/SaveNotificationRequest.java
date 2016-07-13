package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.responses.SaveNotificationResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveNotificationRequest extends BaseRequest<SaveNotificationResponse> {

	private Notification notification;

	@SuppressWarnings("unused")
	private SaveNotificationRequest() {
		// For serialization only
	}

	public SaveNotificationRequest(Notification notification) {
		this.notification = notification;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveNotificationResponse();
	}

	public Notification getNotification() {
		return notification;
	}
}
