package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.requests.GetOutputDocumentsRequest;
import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetOutputDocumentsRequestHandler
		extends
		AbstractActionHandler<GetOutputDocumentsRequest, GetOutputDocumentsResponse> {

	@Override
	public void execute(GetOutputDocumentsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		if (action.getDocumentId() != null) {
			List<OutputDocument> documents = OutputDocumentDaoHelper
					.getDocuments(action.getDocumentId());
			((GetOutputDocumentsResponse) actionResult).setDocuments((ArrayList<OutputDocument>) documents);
		}else if (action.getProcessRefId()!=null){
			List<OutputDocument> documents = OutputDocumentDaoHelper
					.getDocuments(action.getProcessRefId(),action.getSearchTerm());
			((GetOutputDocumentsResponse) actionResult).setDocuments((ArrayList<OutputDocument>) documents);
		}
	}

	public java.lang.Class<GetOutputDocumentsRequest> getActionType() {
		return GetOutputDocumentsRequest.class;
	};
}
