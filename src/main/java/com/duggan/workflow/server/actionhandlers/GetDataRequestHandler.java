package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.requests.GetDataRequest;
import com.duggan.workflow.shared.responses.GetDataResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetDataRequestHandler extends
		AbstractActionHandler<GetDataRequest, GetDataResponse> {

	@Inject
	public GetDataRequestHandler() {
	}

	@Override
	public void execute(GetDataRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		List<DocumentLine> lines = new ArrayList<DocumentLine>();
		if(action.getCatalogRefId()!=null){
			lines = CatalogDaoHelper.getTableData(action.getCatalogRefId(), action.getSearchTerm());
		}else if(action.getCatalogId()!=null){
			lines = CatalogDaoHelper.getTableData(action.getCatalogId(),action.getSearchTerm());
		}
		 
		((GetDataResponse)actionResult).setLines((ArrayList<DocumentLine>) lines);
	}

	@Override
	public Class<GetDataRequest> getActionType() {
		return GetDataRequest.class;
	}
}
