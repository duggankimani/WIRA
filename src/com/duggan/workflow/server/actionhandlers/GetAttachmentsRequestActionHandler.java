package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.AttachmentDaoHelper;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetAttachmentsRequestActionHandler extends
		BaseActionHandler<GetAttachmentsRequest, GetAttachmentsResponse> {

	@Inject
	public GetAttachmentsRequestActionHandler() {
	}

	@Override
	public void execute(GetAttachmentsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetAttachmentsResponse response = (GetAttachmentsResponse)actionResult;
		response.setAttachments(AttachmentDaoHelper.getAttachments(action.getDocumentId()));
	}
	
	@Override
	public Class<GetAttachmentsRequest> getActionType() {
		return GetAttachmentsRequest.class;
	}
}
