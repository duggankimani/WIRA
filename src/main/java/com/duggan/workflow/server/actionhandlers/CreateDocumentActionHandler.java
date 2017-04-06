package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

/**
 * Create Local Documents
 * 
 * @author duggan
 *
 */
public class CreateDocumentActionHandler extends
		AbstractActionHandler<CreateDocumentRequest, CreateDocumentResult> {

	@Inject
	public CreateDocumentActionHandler() {
	}

	@Override
	public void execute(CreateDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
			
		Document doc = new Document();
		
		if(action.getProcessRefId()!=null){
			DocumentType type = DocumentDaoHelper.getDocumentTypeByProcessRef(action.getProcessRefId());
			doc.setType(type);
		}else{
			doc = action.getDocument();
		}

		doc = DocumentDaoHelper.createJson(doc);
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
