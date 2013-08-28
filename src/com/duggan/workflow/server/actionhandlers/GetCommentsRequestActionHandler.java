package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.helper.dao.CommentDaoHelper;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.requests.GetCommentsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetCommentsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetCommentsRequestActionHandler extends
		BaseActionHandler<GetCommentsRequest, GetCommentsResponse> {

	@Inject
	public GetCommentsRequestActionHandler() {
	}

	@Override
	public void execute(GetCommentsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<Comment> comments = CommentDaoHelper.getAllComments(action.getDocumentId());
		
		((GetCommentsResponse)actionResult).setComments(comments);
	}

	@Override
	public Class<GetCommentsRequest> getActionType() {
		return GetCommentsRequest.class;
	}
}