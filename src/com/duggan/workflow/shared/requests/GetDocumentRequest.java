package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDocumentResult;

public class GetDocumentRequest extends BaseRequest<GetDocumentResult> {

	private Long id;

	@SuppressWarnings("unused")
	private GetDocumentRequest() {
		// For serialization only
	}

	public GetDocumentRequest(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public BaseResponse createDefaultActionResponse() {
		return new GetDocumentResult();
	};
}