package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.SearchDocumentRequest;
import com.duggan.workflow.shared.responses.BaseResult;
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
			BaseResult actionResult, ExecutionContext execContext)
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
