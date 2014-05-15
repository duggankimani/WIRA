package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetDocumentTypesRequestActionHandler extends
		BaseActionHandler<GetDocumentTypesRequest, GetDocumentTypesResponse> {

	@Inject
	public GetDocumentTypesRequestActionHandler() {
	}

	@Override
	public void execute(GetDocumentTypesRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetDocumentTypesResponse response = (GetDocumentTypesResponse)actionResult;
		response.setDocumentTypes(DocumentDaoHelper.getDocumentTypes());
	}

	@Override
	public Class<GetDocumentTypesRequest> getActionType() {
		return GetDocumentTypesRequest.class;
	}
}
