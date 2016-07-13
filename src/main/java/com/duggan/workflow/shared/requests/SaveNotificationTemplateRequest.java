package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.TaskNotification;
import com.duggan.workflow.shared.responses.SaveNotificationTemplateResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveNotificationTemplateRequest extends
		BaseRequest<SaveNotificationTemplateResult> {

	TaskNotification notification;
	
	@SuppressWarnings("unused")
	private SaveNotificationTemplateRequest() {
		// For serialization only
	}

	public SaveNotificationTemplateRequest(TaskNotification notification) {
		this.notification = notification;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new SaveNotificationTemplateResult();
	}

	public TaskNotification getNotification() {
		return notification;
	}

}
