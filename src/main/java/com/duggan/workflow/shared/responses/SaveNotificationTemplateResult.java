package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.TaskNotification;


public class SaveNotificationTemplateResult extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TaskNotification notification;

	public SaveNotificationTemplateResult() {
		// For serialization only
	}

	public TaskNotification getNotification() {
		return notification;
	}

	public void setNotification(TaskNotification notification) {
		this.notification = notification;
	}

}
