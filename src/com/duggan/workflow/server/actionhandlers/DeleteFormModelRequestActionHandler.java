package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.FormDaoHelper;
import com.duggan.workflow.shared.requests.DeleteFormModelRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.DeleteFormModelResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteFormModelRequestActionHandler extends
		BaseActionHandler<DeleteFormModelRequest, DeleteFormModelResponse> {

	@Inject
	public DeleteFormModelRequestActionHandler() {
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
