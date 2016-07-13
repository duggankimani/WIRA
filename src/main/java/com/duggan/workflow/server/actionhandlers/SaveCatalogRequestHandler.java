package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.requests.SaveCatalogRequest;
import com.duggan.workflow.shared.responses.SaveCatalogResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class SaveCatalogRequestHandler extends
		AbstractActionHandler<SaveCatalogRequest, SaveCatalogResponse> {

	@Inject
	public SaveCatalogRequestHandler() {
	}

	@Override
	public void execute(SaveCatalogRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Catalog catalog = action.getCatalog();
		catalog = CatalogDaoHelper.save(catalog);
		
		SaveCatalogResponse response = (SaveCatalogResponse)actionResult;
		response.setCatalog(catalog);
	}

	@Override
	public Class<SaveCatalogRequest> getActionType() {
		return SaveCatalogRequest.class;
	}
}
