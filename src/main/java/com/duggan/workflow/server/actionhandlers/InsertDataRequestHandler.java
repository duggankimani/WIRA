package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.requests.InsertDataRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class InsertDataRequestHandler extends
		BaseActionHandler<InsertDataRequest, BaseResponse> {

	@Inject
	public InsertDataRequestHandler() {
	}

	@Override
	public void execute(InsertDataRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		CatalogDaoHelper.saveData(action.getCatalogId(), action.getLines());
	}

	@Override
	public Class<InsertDataRequest> getActionType() {
		return InsertDataRequest.class;
	}
}
