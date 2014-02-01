package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.requests.DeleteDocumentRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.DeleteDocumentResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteDocumentRequestHandler extends
		BaseActionHandler<DeleteDocumentRequest, DeleteDocumentResponse> {

	@Inject
	public DeleteDocumentRequestHandler() {
	}

	@Override
	public void execute(DeleteDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		Long documentId = action.getDocumentId();
		assert documentId!=null;
		boolean deleted = DB.getDocumentDao().deleteDocument(documentId);
		
		DeleteDocumentResponse response = (DeleteDocumentResponse)actionResult;
		response.setDelete(deleted);
	}

	@Override
	public Class<DeleteDocumentRequest> getActionType() {
		return DeleteDocumentRequest.class;
	}
}
