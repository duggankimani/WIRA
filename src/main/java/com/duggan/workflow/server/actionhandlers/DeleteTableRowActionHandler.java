package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.requests.DeleteTableRowRequest;
import com.duggan.workflow.shared.responses.DeleteTableRowResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteTableRowActionHandler extends
		AbstractActionHandler<DeleteTableRowRequest, DeleteTableRowResponse> {

	@Inject
	public DeleteTableRowActionHandler() {
	}

	@Override
	public void execute(DeleteTableRowRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		String fieldRef = action.getFieldRefId();
		int row = action.getRow();
		
		Field rtnfield = FormDaoHelper.deleteTableRowJson(fieldRef,row);
		
		DeleteTableRowResponse response = (DeleteTableRowResponse)actionResult;
		
		response.setField(rtnfield);
	}

	@Override
	public Class<DeleteTableRowRequest> getActionType() {
		return DeleteTableRowRequest.class;
	}
}
