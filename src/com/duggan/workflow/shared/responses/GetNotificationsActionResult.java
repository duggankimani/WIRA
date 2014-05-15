package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.Notification;

public class GetNotificationsActionResult extends BaseResponse{

	private List<Notification> notifications;

	public GetNotificationsActionResult() {
		// For serialization only
	}

	public GetNotificationsActionResult(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
}
