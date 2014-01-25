package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.UpdateNotificationRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.UpdateNotificationRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class UpdateNotificationRequestActionHandler extends
		BaseActionHandler<UpdateNotificationRequest, UpdateNotificationRequestResult> {

	@Inject
	public UpdateNotificationRequestActionHandler() {
	}
	
	@Override
	public void execute(UpdateNotificationRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Long noteId = action.getNotificationId();
		Boolean isRead = action.getRead();
		
		NotificationDaoHelper.updateNotification(noteId, isRead);
		
		Notification notification = NotificationDaoHelper.getNotification(noteId);
		UpdateNotificationRequestResult result = (UpdateNotificationRequestResult) actionResult;
		result.setNotification(notification);
				
	}

	@Override
	public Class<UpdateNotificationRequest> getActionType() {
		return UpdateNotificationRequest.class;
	}
}
