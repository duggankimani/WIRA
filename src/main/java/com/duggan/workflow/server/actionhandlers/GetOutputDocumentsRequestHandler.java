package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.requests.GetOutputDocumentsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetOutputDocumentsRequestHandler extends AbstractActionHandler<GetOutputDocumentsRequest, GetOutputDocumentsResponse>{

	@Override
	public void execute(GetOutputDocumentsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		List<OutputDocument> documents =  OutputDocumentDaoHelper.getDocuments(action.getDocumentId());
		((GetOutputDocumentsResponse)actionResult).setDocuments(documents);
	}
	
	public java.lang.Class<GetOutputDocumentsRequest> getActionType() {
		return GetOutputDocumentsRequest.class;
	};
}
