package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Notification;
import com.wira.commons.shared.response.BaseResponse;

public class UpdateNotificationRequestResult extends BaseResponse {

	Notification notification;
	
	public UpdateNotificationRequestResult() {
		
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}
}
