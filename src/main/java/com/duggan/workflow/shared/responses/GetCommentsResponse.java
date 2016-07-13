package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Comment;
import com.wira.commons.shared.response.BaseResponse;

public class GetCommentsResponse extends BaseResponse {

	private ArrayList<Comment> comments;

	public GetCommentsResponse() {
		// For serialization only
	}

	public GetCommentsResponse(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
}
