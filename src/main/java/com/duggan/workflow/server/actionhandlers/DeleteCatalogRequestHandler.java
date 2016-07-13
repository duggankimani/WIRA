package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;
import com.duggan.workflow.shared.model.catalog.IsCatalogItem;
import com.duggan.workflow.shared.requests.DeleteCatalogRequest;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteCatalogRequestHandler extends
		AbstractActionHandler<DeleteCatalogRequest, BaseResponse> {

	@Inject
	public DeleteCatalogRequestHandler() {
	}
	
	@Override
	public void execute(DeleteCatalogRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		IsCatalogItem item = action.getItem();
		if(item instanceof Catalog){
			CatalogDaoHelper.deleteCatalog(((Catalog) item).getId());
		}else if(item instanceof CatalogColumn){
			CatalogDaoHelper.deleteCatalogColumn(((CatalogColumn) item).getId());
		}
		
	}
	
	@Override
	public Class<DeleteCatalogRequest> getActionType() {
		return DeleteCatalogRequest.class;
	}
}
