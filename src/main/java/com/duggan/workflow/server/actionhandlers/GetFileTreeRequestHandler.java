package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.requests.GetFileTreeRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetFileTreeResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetFileTreeRequestHandler extends
		AbstractActionHandler<GetFileTreeRequest, GetFileTreeResponse> {

	@Inject
	public GetFileTreeRequestHandler() {
	}

	@Override
	public void execute(GetFileTreeRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetFileTreeResponse response = (GetFileTreeResponse) actionResult;

		switch (action.getType()) {
		case FILES:
			response.setAttachments((ArrayList<Attachment>) DB.getAttachmentDao().getFileTree());
			break;
		case PROCESSES:
			response.setAttachments((ArrayList<Attachment>) DB.getAttachmentDao().getFileProcessTree());
			break;
		case USERS:
			response.setAttachments((ArrayList<Attachment>) DB.getAttachmentDao().getFileUserTree());
			break;

		default:
			response.setAttachments(new ArrayList<Attachment>());
			break;
		}

	}

	@Override
	public Class<GetFileTreeRequest> getActionType() {
		return GetFileTreeRequest.class;
	}
}
