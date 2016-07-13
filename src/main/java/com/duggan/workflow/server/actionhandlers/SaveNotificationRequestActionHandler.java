package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.SaveNotificationRequest;
import com.duggan.workflow.shared.responses.SaveNotificationResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class SaveNotificationRequestActionHandler extends
		AbstractActionHandler<SaveNotificationRequest, SaveNotificationResponse> {

	@Inject
	public SaveNotificationRequestActionHandler() {
	}

	@Override
	public void execute(SaveNotificationRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Notification note = action.getNotification();
		note = NotificationDaoHelper.saveNotification(note);
		
		SaveNotificationResponse response = (SaveNotificationResponse)actionResult;
		response.setNotification(note);
	}

	@Override
	public Class<SaveNotificationRequest> getActionType() {
		return SaveNotificationRequest.class;
	}
}
