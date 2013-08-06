package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Notification;

public class SaveNotificationRequestResult extends BaseResult {

	Notification notification;
	
	public SaveNotificationRequestResult() {
		
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}
}
