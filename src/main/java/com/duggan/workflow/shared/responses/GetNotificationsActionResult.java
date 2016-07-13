package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Notification;
import com.wira.commons.shared.response.BaseResponse;

public class GetNotificationsActionResult extends BaseResponse{

	private ArrayList<Notification> notifications;

	public GetNotificationsActionResult() {
		// For serialization only
	}

	public GetNotificationsActionResult(ArrayList<Notification> notifications) {
		this.notifications = notifications;
	}

	public ArrayList<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(ArrayList<Notification> notifications) {
		this.notifications = notifications;
	}
}
