package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.requests.AssignTaskRequest;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;

public class AssignTaskActionHandler extends
		AbstractActionHandler<AssignTaskRequest, BaseResponse> {

	@Inject
	public AssignTaskActionHandler() {
	}

	@Override
	public void execute(AssignTaskRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		Long taskId = action.getTaskId();
		String userId = action.getUserId();
		HTUser user = SessionHelper.getCurrentUser();
		String username = null;
		boolean isAdmin  = false;
		if(user!=null) {
			isAdmin = user.isAdmin();
			username = user.getUserId();
		}
		
		if(isAdmin) {
			JBPMHelper.get().reassignTaskAsAdmin(taskId, userId);
		}else {
			JBPMHelper.get().reassignTask(taskId,username, userId);
		}
		
	}
	
	@Override
	public Class<AssignTaskRequest> getActionType() {
		return AssignTaskRequest.class;
	}
}
