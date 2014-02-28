package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.GetNotificationsAction;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetNotificationsActionResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetNotificationsActionHandler extends
		BaseActionHandler<GetNotificationsAction, GetNotificationsActionResult> {

	@Inject
	public GetNotificationsActionHandler() {
	}
	
	@Override
	public void execute(GetNotificationsAction action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		List<Notification> notifications = NotificationDaoHelper.getAllNotifications(action.getUserId());
		
		GetNotificationsActionResult result = (GetNotificationsActionResult)actionResult;
		result.setNotifications(notifications);
		
	}
	
	@Override
	public Class<GetNotificationsAction> getActionType() {
		return GetNotificationsAction.class;
	}
}
