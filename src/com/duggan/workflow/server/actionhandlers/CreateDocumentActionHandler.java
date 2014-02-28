package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
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
	public void execute(CreateDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
				
		Document doc = action.getDocument();
		
		doc = DocumentDaoHelper.save(doc);
				
		CreateDocumentResult result = (CreateDocumentResult)actionResult;
		result.setDocument(doc);
		
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
