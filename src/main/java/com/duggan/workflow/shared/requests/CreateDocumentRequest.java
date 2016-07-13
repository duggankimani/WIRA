package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class CreateDocumentRequest extends
		BaseRequest<CreateDocumentResult> {

	private Document document;

	@SuppressWarnings("unused")
	private CreateDocumentRequest() {
		
	}

	public CreateDocumentRequest(Document document) {
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new CreateDocumentResult();
	}
}
