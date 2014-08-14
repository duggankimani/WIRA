package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;

public class GetAttachmentsRequest extends
		BaseRequest<GetAttachmentsResponse> {

	private Long documentId;
	private String userId;

	@SuppressWarnings("unused")
	private GetAttachmentsRequest() {
		// For serialization only
	}

	public GetAttachmentsRequest(Long documentId) {
		this.documentId = documentId;
	}
	
	public GetAttachmentsRequest(String userId){
		this.userId= userId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetAttachmentsResponse();
	}

	public String getUserId() {
		return userId;
	}
}
