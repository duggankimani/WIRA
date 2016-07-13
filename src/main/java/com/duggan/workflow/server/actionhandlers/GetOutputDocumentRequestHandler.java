package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.requests.GetOutputDocumentRequest;
import com.duggan.workflow.shared.responses.GetOutputDocumentResponse;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetOutputDocumentRequestHandler
		extends
		AbstractActionHandler<GetOutputDocumentRequest, GetOutputDocumentResponse> {

	@Override
	public void execute(GetOutputDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		OutputDocument doc = OutputDocumentDaoHelper.getDocument(action.getOutputDocId(),action.isLoadTemplate());
		((GetOutputDocumentResponse)actionResult).setDocument(doc);
		
	}

	public java.lang.Class<GetOutputDocumentRequest> getActionType() {
		return GetOutputDocumentRequest.class;
	};
}
