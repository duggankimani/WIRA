package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.requests.DeleteNotificationTemplateRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteNotificationTemplateRequestHandler extends
		BaseActionHandler<DeleteNotificationTemplateRequest, BaseResponse> {

	@Override
	public Class<DeleteNotificationTemplateRequest> getActionType() {
		return DeleteNotificationTemplateRequest.class;
	}

	@Override
	public void execute(DeleteNotificationTemplateRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		ProcessDefHelper.deleteTaskNotification(action.getNotificationId());
	}

}
