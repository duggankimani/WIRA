package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.requests.DeleteNotificationTemplateRequest;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteNotificationTemplateRequestHandler extends
		AbstractActionHandler<DeleteNotificationTemplateRequest, BaseResponse> {

	@Override
	public Class<DeleteNotificationTemplateRequest> getActionType() {
		return DeleteNotificationTemplateRequest.class;
	}

	@Override
	public void execute(DeleteNotificationTemplateRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		ProcessDaoHelper.deleteTaskNotification(action.getNotificationId());
	}

}
