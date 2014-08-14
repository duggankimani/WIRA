package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;

public class GetOutputDocumentsRequest extends BaseRequest<GetOutputDocumentsResponse> {

	private Long documentId;

	public GetOutputDocumentsRequest() {
	}
	
	public GetOutputDocumentsRequest(Long id) {
		this.documentId = id;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetOutputDocumentsResponse();
	}

}
