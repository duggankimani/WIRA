package com.duggan.workflow.server.actionhandlers;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.duggan.workflow.server.helper.dao.ProcessDefHelper;
import com.duggan.workflow.shared.requests.DeleteProcessRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.DeleteProcessResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteProcessRequestActionHandler extends
		BaseActionHandler<DeleteProcessRequest, DeleteProcessResponse> {

	@Inject
	public DeleteProcessRequestActionHandler() {
	}
	
	@Override
	public void execute(DeleteProcessRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		ProcessDefHelper.delete(action.getProcessId());
		
	}
	
	@Override
	public Class<DeleteProcessRequest> getActionType() {
		return DeleteProcessRequest.class;
	}
}
