package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveCommentResponse;

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
