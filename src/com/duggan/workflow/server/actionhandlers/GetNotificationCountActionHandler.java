package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.shared.requests.GetNotificationCount;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetNotificationCountResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetNotificationCountActionHandler extends
		BaseActionHandler<GetNotificationCount, GetNotificationCountResult> {

	@Inject
	public GetNotificationCountActionHandler() {
	}
	
	@Override
	public void execute(GetNotificationCount action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Integer count = NotificationDaoHelper.getNotificationCount(action.getUserId());
		
		GetNotificationCountResult result = (GetNotificationCountResult)actionResult;
		result.setCount(count);
		
	}
	
	@Override
	public Class<GetNotificationCount> getActionType() {
		return GetNotificationCount.class;
	}
}

