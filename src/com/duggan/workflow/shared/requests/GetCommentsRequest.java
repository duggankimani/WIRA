package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetCommentsResponse;

public class GetCommentsRequest extends BaseRequest<GetCommentsResponse> {

	private Long documentId;

	@SuppressWarnings("unused")
	private GetCommentsRequest() {
		// For serialization only
	}

	public GetCommentsRequest(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetCommentsResponse();
	}
}
