package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.CreateDocumentResult;

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
