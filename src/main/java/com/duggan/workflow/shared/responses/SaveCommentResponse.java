package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Comment;

public class SaveCommentResponse extends BaseResponse {

	private Comment comment;

	public SaveCommentResponse() {
	}

	public SaveCommentResponse(Comment comment) {
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
