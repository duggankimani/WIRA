package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.responses.DeleteLineResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class DeleteLineRequest extends BaseRequest<DeleteLineResponse> {

	private DocumentLine line;

	@SuppressWarnings("unused")
	private DeleteLineRequest() {
		// For serialization only
	}

	public DeleteLineRequest(DocumentLine line) {
		this.line = line;
	}

	public DocumentLine getLine() {
		return line;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new DeleteLineResponse();
	}
}
