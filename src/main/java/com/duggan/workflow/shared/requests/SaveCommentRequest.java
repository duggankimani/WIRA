package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.responses.SaveCommentResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveCommentRequest extends BaseRequest<SaveCommentResponse> {

	private Comment comment;

	@SuppressWarnings("unused")
	private SaveCommentRequest() {
		// For serialization only
	}

	public SaveCommentRequest(Comment comment) {
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveCommentResponse();
	}
}
