package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.AttachmentDaoHelper;
import com.duggan.workflow.shared.requests.DeleteAttachmentRequest;
import com.duggan.workflow.shared.responses.DeleteAttachmentResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteAttachmentRequestHandler extends
		AbstractActionHandler<DeleteAttachmentRequest, DeleteAttachmentResponse> {

	@Inject
	public DeleteAttachmentRequestHandler() {
	}

	@Override
	public void execute(DeleteAttachmentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		boolean deleted = false;
		if(action.getAttachmentId()!=null){
			deleted = AttachmentDaoHelper.delete(action.getAttachmentId());
		}
		
		if(action.getAttachmentIds()!=null){
			deleted = AttachmentDaoHelper.delete(action.getAttachmentIds());
		}
		
		DeleteAttachmentResponse response = (DeleteAttachmentResponse)actionResult;
		response.setIsDeleted(deleted);
	}

	@Override
	public Class<DeleteAttachmentRequest> getActionType() {
		return DeleteAttachmentRequest.class;
	}
}
