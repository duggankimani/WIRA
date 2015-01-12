package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.TaskNotification;
import com.duggan.workflow.shared.requests.GetNotificationTemplateRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetNotificationTemplateResult;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetNotificationTemplateRequestHandler extends
		BaseActionHandler<GetNotificationTemplateRequest, GetNotificationTemplateResult> {

	@Override
	public Class<GetNotificationTemplateRequest> getActionType() {
		return GetNotificationTemplateRequest.class;
	}

	@Override
	public void execute(GetNotificationTemplateRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		TaskNotification notification = ProcessDefHelper.getTaskNotificationTemplate(
				action.getNodeId(), action.getStepName(),
				action.getProcessDefId(), action.getCategory(),
				action.getAction());
		((GetNotificationTemplateResult)actionResult).setNotification(notification);
	}

}
