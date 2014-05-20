package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;

public class GetAttachmentsRequest extends
		BaseRequest<GetAttachmentsResponse> {

	private Long documentId;

	@SuppressWarnings("unused")
	private GetAttachmentsRequest() {
		// For serialization only
	}

	public GetAttachmentsRequest(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetAttachmentsResponse();
	}
}
