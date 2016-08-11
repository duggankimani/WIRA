package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.shared.requests.DeleteDocumentRequest;
import com.duggan.workflow.shared.responses.DeleteDocumentResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteDocumentRequestHandler extends
		AbstractActionHandler<DeleteDocumentRequest, DeleteDocumentResponse> {

	@Inject
	public DeleteDocumentRequestHandler() {
	}

	@Override
	public void execute(DeleteDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		String docRefId = action.getDocRefId();
		assert docRefId!=null;
//		boolean deleted = DB.getDocumentDao().deleteDocument(docRefId);
		DocumentDaoHelper.deleteJsonDoc(docRefId);
		
		DeleteDocumentResponse response = (DeleteDocumentResponse)actionResult;
		response.setDelete(true);
	}

	@Override
	public Class<DeleteDocumentRequest> getActionType() {
		return DeleteDocumentRequest.class;
	}
}
