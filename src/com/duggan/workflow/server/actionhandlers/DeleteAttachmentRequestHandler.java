package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.AttachmentDaoHelper;
import com.duggan.workflow.shared.requests.DeleteAttachmentRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.DeleteAttachmentResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteAttachmentRequestHandler extends
		BaseActionHandler<DeleteAttachmentRequest, DeleteAttachmentResponse> {

	@Inject
	public DeleteAttachmentRequestHandler() {
	}

	@Override
	public void execute(DeleteAttachmentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		boolean deleted = AttachmentDaoHelper.delete(action.getAttachmentId());
		
		DeleteAttachmentResponse response = (DeleteAttachmentResponse)actionResult;
		response.setIsDeleted(deleted);
	}

	@Override
	public Class<DeleteAttachmentRequest> getActionType() {
		return DeleteAttachmentRequest.class;
	}
}
