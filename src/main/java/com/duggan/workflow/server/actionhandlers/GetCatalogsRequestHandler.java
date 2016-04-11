package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.requests.GetCatalogsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetCatalogsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetCatalogsRequestHandler extends
		AbstractActionHandler<GetCatalogsRequest, GetCatalogsResponse> {

	@Inject
	public GetCatalogsRequestHandler() {
	}

	@Override
	public void execute(GetCatalogsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		List<Catalog> catalogs = new ArrayList<>();
		
		if(action.getCatalogId()!=null){
			Catalog cat = CatalogDaoHelper.getCatalog(action.getCatalogId());
			catalogs.add(cat);
		}else if(action.isLoadViews()){
			catalogs = CatalogDaoHelper.getAllViews();
		}else{
			catalogs = CatalogDaoHelper.getAllCatalogs();
		}
		
		
		((GetCatalogsResponse)actionResult).setCatalogs(catalogs);
	}

	@Override
	public Class<GetCatalogsRequest> getActionType() {
		return GetCatalogsRequest.class;
	}
}
