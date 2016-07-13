package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.shared.requests.InsertDataRequest;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class InsertDataRequestHandler extends
		AbstractActionHandler<InsertDataRequest, BaseResponse> {

	@Inject
	public InsertDataRequestHandler() {
	}

	@Override
	public void execute(InsertDataRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		CatalogDaoHelper.saveData(action.getCatalogId(), action.getLines(),true);
	}

	@Override
	public Class<InsertDataRequest> getActionType() {
		return InsertDataRequest.class;
	}
}
