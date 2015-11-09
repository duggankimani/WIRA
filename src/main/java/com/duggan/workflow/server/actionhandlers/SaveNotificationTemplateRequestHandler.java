package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.TaskNotification;
import com.duggan.workflow.shared.requests.SaveNotificationTemplateRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveNotificationTemplateResult;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveNotificationTemplateRequestHandler extends
		AbstractActionHandler<SaveNotificationTemplateRequest, SaveNotificationTemplateResult> {

	@Override
	public Class<SaveNotificationTemplateRequest> getActionType() {
		return SaveNotificationTemplateRequest.class;
	}

	@Override
	public void execute(SaveNotificationTemplateRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		TaskNotification notification = ProcessDefHelper.saveTaskNotification(action.getNotification());
		((SaveNotificationTemplateResult)actionResult).setNotification(notification);
	}

}
