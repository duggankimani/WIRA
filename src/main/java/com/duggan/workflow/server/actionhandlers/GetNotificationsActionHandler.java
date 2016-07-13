package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.GetNotificationsAction;
import com.duggan.workflow.shared.responses.GetNotificationsActionResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetNotificationsActionHandler extends
		AbstractActionHandler<GetNotificationsAction, GetNotificationsActionResult> {

	@Inject
	public GetNotificationsActionHandler() {
	}
	
	@Override
	public void execute(GetNotificationsAction action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		List<Notification> notifications = NotificationDaoHelper.getAllNotifications(action.getUserId());
		
		GetNotificationsActionResult result = (GetNotificationsActionResult)actionResult;
		result.setNotifications((ArrayList<Notification>) notifications);
		
	}
	
	@Override
	public Class<GetNotificationsAction> getActionType() {
		return GetNotificationsAction.class;
	}
}
