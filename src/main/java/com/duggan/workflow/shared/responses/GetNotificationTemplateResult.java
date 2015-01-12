package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.TaskNotification;


public class GetNotificationTemplateResult extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TaskNotification notification;

	public GetNotificationTemplateResult() {
		// For serialization only
	}

	public TaskNotification getNotification() {
		return notification;
	}

	public void setNotification(TaskNotification notification) {
		this.notification = notification;
	}

}
