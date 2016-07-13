package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetDocumentTypesRequestActionHandler extends
		AbstractActionHandler<GetDocumentTypesRequest, GetDocumentTypesResponse> {

	@Inject
	public GetDocumentTypesRequestActionHandler() {
	}

	@Override
	public void execute(GetDocumentTypesRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetDocumentTypesResponse response = (GetDocumentTypesResponse)actionResult;
		response.setDocumentTypes((ArrayList<DocumentType>) DocumentDaoHelper.getDocumentTypes());
	}

	@Override
	public Class<GetDocumentTypesRequest> getActionType() {
		return GetDocumentTypesRequest.class;
	}
}
