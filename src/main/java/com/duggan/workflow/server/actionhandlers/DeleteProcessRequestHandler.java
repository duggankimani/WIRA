package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.requests.DeleteProcessRequest;
import com.duggan.workflow.shared.responses.DeleteProcessResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteProcessRequestHandler extends
		AbstractActionHandler<DeleteProcessRequest, DeleteProcessResponse> {

	@Inject
	public DeleteProcessRequestHandler() {
	}
	
	@Override
	public void execute(DeleteProcessRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		ProcessDaoHelper.delete(action.getProcessId());
		
	}
	
	@Override
	public Class<DeleteProcessRequest> getActionType() {
		return DeleteProcessRequest.class;
	}
}
