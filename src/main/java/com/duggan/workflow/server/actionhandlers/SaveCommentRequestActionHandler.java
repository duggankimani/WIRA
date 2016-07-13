package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.CommentDaoHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.requests.SaveCommentRequest;
import com.duggan.workflow.shared.responses.SaveCommentResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class SaveCommentRequestActionHandler extends
		AbstractActionHandler<SaveCommentRequest, SaveCommentResponse> {

	@Inject
	public SaveCommentRequestActionHandler() {
	}

	@Override
	public void execute(SaveCommentRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Comment comment = action.getComment();
		comment.setCreatedBy(SessionHelper.getCurrentUser());
		comment = CommentDaoHelper.saveComment(comment);
		
		SaveCommentResponse response = (SaveCommentResponse)actionResult;
		response.setComment(comment);
	}

	@Override
	public Class<SaveCommentRequest> getActionType() {
		return SaveCommentRequest.class;
	}
}
