package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.requests.GetCatalogsRequest;
import com.duggan.workflow.shared.responses.GetCatalogsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetCatalogsRequestHandler extends
		AbstractActionHandler<GetCatalogsRequest, GetCatalogsResponse> {

	@Inject
	public GetCatalogsRequestHandler() {
	}

	@Override
	public void execute(GetCatalogsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		ArrayList<Catalog> catalogs = new ArrayList<>();
		
		if(action.getCatalogRefId()!=null){
			Catalog cat = CatalogDaoHelper.getCatalog(action.getCatalogRefId());
			catalogs.add(cat);
		}else if(action.isLoadViews()){
			catalogs.addAll(CatalogDaoHelper.getAllViews());
		}else{
			catalogs.addAll(CatalogDaoHelper.getAllCatalogs(action.getSearchTerm()));
		}
		
		
		((GetCatalogsResponse)actionResult).setCatalogs(catalogs);
	}

	@Override
	public Class<GetCatalogsRequest> getActionType() {
		return GetCatalogsRequest.class;
	}
}
