package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.requests.SaveOutputDocumentRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveOutputDocumentResponse;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveOuputDocumentRequestHandler extends BaseActionHandler<SaveOutputDocumentRequest, SaveOutputDocumentResponse> {
	
	@Override
	public void execute(SaveOutputDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		OutputDocument doc = OutputDocumentDaoHelper.saveOutputDoc(action.getDoc());
		((SaveOutputDocumentResponse)actionResult).setDoc(doc);
	}
	
	@Override
	public Class<SaveOutputDocumentRequest> getActionType() {
		return SaveOutputDocumentRequest.class;
	}
}
