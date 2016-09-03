package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.helper.AttachmentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetAttachmentsRequestHandler extends
		AbstractActionHandler<GetAttachmentsRequest, GetAttachmentsResponse> {

	@Inject
	public GetAttachmentsRequestHandler() {
	}

	@Override
	public void execute(GetAttachmentsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetAttachmentsResponse response = (GetAttachmentsResponse) actionResult;

		if (action.getType() != null) {
			response.setAttachments((ArrayList<Attachment>) DB.getAttachmentDao().getAttachments(action.getType(),
					action.getRefId(), action.getSearchTerm()));
		} 
//		else if (action.getDocumentId() != null) {
//			response.setAttachments((ArrayList<Attachment>)AttachmentDaoHelper.getAttachments(action
//					.getDocumentId()));
//		}
		else if (action.getDocRefId() != null) {
			response.setAttachments((ArrayList<Attachment>)AttachmentDaoHelper
					.getAttachmentsByDocRefId(action.getDocRefId()));
		} else if (action.getUserId() != null) {
			response.setAttachments((ArrayList<Attachment>)AttachmentDaoHelper.getAllAttachments(
					action.getUserId(), true));
		} else {
			// TODO: Determine default behavior
			response.setAttachments((ArrayList<Attachment>)AttachmentDaoHelper.getAllAttachments());
		}

	}

	@Override
	public Class<GetAttachmentsRequest> getActionType() {
		return GetAttachmentsRequest.class;
	}
}
