package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.SearchDocumentRequestResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SearchDocumentRequest extends
		BaseRequest<SearchDocumentRequestResult> {

	private String subject;
	private Long documentId;

	@SuppressWarnings("unused")
	private SearchDocumentRequest() {
		// For serialization only
	}

	public SearchDocumentRequest(String subject, Long documentId) {
		this.subject = subject;
		this.documentId = documentId;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SearchDocumentRequestResult();
	}
	
	public String getSubject() {
		return subject;
	}

	public Long getDocumentId() {
		return documentId;
	}
}
