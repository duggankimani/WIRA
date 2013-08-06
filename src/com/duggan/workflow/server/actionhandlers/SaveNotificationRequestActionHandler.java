package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.SaveNotificationRequest;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.SaveNotificationRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveNotificationRequestActionHandler extends
		BaseActionHandler<SaveNotificationRequest, SaveNotificationRequestResult> {

	@Inject
	public SaveNotificationRequestActionHandler() {
	}
	
	@Override
	public void execute(SaveNotificationRequest action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Long noteId = action.getNotificationId();
		Boolean isRead = action.getRead();
		
		NotificationDaoHelper.updateNotification(noteId, isRead);
		
		Notification notification = NotificationDaoHelper.getNotification(noteId);
		SaveNotificationRequestResult result = (SaveNotificationRequestResult) actionResult;
		result.setNotification(notification);
				
	}

	@Override
	public Class<SaveNotificationRequest> getActionType() {
		return SaveNotificationRequest.class;
	}
}
