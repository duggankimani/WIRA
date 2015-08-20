package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.DeleteDocumentResponse;

public class DeleteDocumentRequest extends
		BaseRequest<DeleteDocumentResponse> {

	private Long documentId;
	private String docRefId;

	@SuppressWarnings("unused")
	private DeleteDocumentRequest() {
		// For serialization only
	}
	
	public DeleteDocumentRequest(String docRefId) {
		this.docRefId = docRefId;
	}
	
	public String getDocRefId() {
		return docRefId;
	}

	@Deprecated
	public DeleteDocumentRequest(Long documentId) {
		this.documentId = documentId;
	}

	@Deprecated
	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new DeleteDocumentResponse();
	}
}
