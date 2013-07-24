package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * Create Local Documents
 * 
 * @author duggan
 *
 */
public class CreateDocumentActionHandler extends
		BaseActionHandler<CreateDocumentRequest, CreateDocumentResult> {

	@Inject
	public CreateDocumentActionHandler() {
	}

	@Override
	public CreateDocumentResult execute(CreateDocumentRequest action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {
				
		Document doc = action.getDocument();
		
		doc = DocumentDaoHelper.save(doc);
				
		CreateDocumentResult result = (CreateDocumentResult)actionResult;
		result.setDocument(doc);
		
		return result;
	}

	@Override
	public void undo(CreateDocumentRequest action,
			CreateDocumentResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<CreateDocumentRequest> getActionType() {
		return CreateDocumentRequest.class;
	}
}
