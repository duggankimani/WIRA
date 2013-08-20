package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Comment;
import java.util.List;

public class GetCommentsResponse extends BaseResponse {

	private List<Comment> comments;

	public GetCommentsResponse() {
		// For serialization only
	}

	public GetCommentsResponse(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
