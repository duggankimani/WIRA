package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.shared.requests.DeleteFormModelRequest;
import com.duggan.workflow.shared.responses.DeleteFormModelResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteFormModelRequestHandler extends
		AbstractActionHandler<DeleteFormModelRequest, DeleteFormModelResponse> {

	@Inject
	public DeleteFormModelRequestHandler() {
	}

	@Override
	public void execute(DeleteFormModelRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		FormDaoHelper.delete(action.getModel());
	}

	@Override
	public Class<DeleteFormModelRequest> getActionType() {
		return DeleteFormModelRequest.class;
	}
}
