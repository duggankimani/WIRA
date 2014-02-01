package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.DeleteDocumentResponse;

import java.lang.Long;

public class DeleteDocumentRequest extends
		BaseRequest<DeleteDocumentResponse> {

	private Long documentId;

	@SuppressWarnings("unused")
	private DeleteDocumentRequest() {
		// For serialization only
	}

	public DeleteDocumentRequest(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new DeleteDocumentResponse();
	}
}
