package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.shared.requests.DeleteLineRequest;
import com.duggan.workflow.shared.responses.DeleteLineResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteLineRequestActionHandler extends
		AbstractActionHandler<DeleteLineRequest, DeleteLineResponse> {

	@Inject
	public DeleteLineRequestActionHandler() {
	}

	@Override
	public void execute(DeleteLineRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		//DocumentDaoHelper.delete(action.getLine());
		
		if(action.getLine().getRefId()!=null){
			DocumentDaoHelper.deleteJsonDocLine(action.getLine().getRefId());
		}
		
		DeleteLineResponse response = (DeleteLineResponse)actionResult;
		response.setDelete(true);
	}

	@Override
	public void undo(DeleteLineRequest action, DeleteLineResponse result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<DeleteLineRequest> getActionType() {
		return DeleteLineRequest.class;
	}
}
