package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.SearchDocumentRequest;
import com.duggan.workflow.shared.responses.SearchDocumentRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class SearchDocumentRequestActionHandler extends
		AbstractActionHandler<SearchDocumentRequest, SearchDocumentRequestResult> {

	@Inject
	public SearchDocumentRequestActionHandler() {
	}

	@Override
	public void execute(SearchDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		SearchDocumentRequestResult result = (SearchDocumentRequestResult)actionResult;
		
		List<Document> notes = DocumentDaoHelper.search(action.getSubject());
		
		result.setDocument((ArrayList<Document>) notes);
		
	}
	
	@Override
	public Class<SearchDocumentRequest> getActionType() {
		return SearchDocumentRequest.class;
	}
}
