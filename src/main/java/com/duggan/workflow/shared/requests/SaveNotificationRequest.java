package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveNotificationResponse;

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
