package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetDocumentResult;

import java.lang.Integer;

public class GetDocumentRequest extends BaseRequest<GetDocumentResult> {

	private Integer id;

	@SuppressWarnings("unused")
	private GetDocumentRequest() {
		// For serialization only
	}

	public GetDocumentRequest(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public BaseResult createDefaultActionResponse() {
		return new GetDocumentResult();
	};
}