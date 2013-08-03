package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetDocumentResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * 
 * @author duggan
 *
 */
public class GetDocumentRequestHandler extends
		BaseActionHandler<GetDocumentRequest, GetDocumentResult> {

	@Inject
	public GetDocumentRequestHandler() {
	}

	@Override
	public void execute(GetDocumentRequest action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Document document = DocumentDaoHelper.getDocument(action.getId());
		
		GetDocumentResult result = (GetDocumentResult)actionResult;
		result.setDocument(document);		
		
	}

	@Override
	public void undo(GetDocumentRequest action, GetDocumentResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetDocumentRequest> getActionType() {
		return GetDocumentRequest.class;
	}
}
