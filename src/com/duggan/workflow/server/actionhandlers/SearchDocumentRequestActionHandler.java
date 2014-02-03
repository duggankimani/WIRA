package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.SearchDocumentRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SearchDocumentRequestResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SearchDocumentRequestActionHandler extends
		BaseActionHandler<SearchDocumentRequest, SearchDocumentRequestResult> {

	@Inject
	public SearchDocumentRequestActionHandler() {
	}

	@Override
	public void execute(SearchDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		SearchDocumentRequestResult result = (SearchDocumentRequestResult)actionResult;
		
		List<Document> notes = DocumentDaoHelper.search(action.getSubject());
		
		result.setDocument(notes);
		
	}
	
	@Override
	public Class<SearchDocumentRequest> getActionType() {
		return SearchDocumentRequest.class;
	}
}
