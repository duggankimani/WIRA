package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.duggan.workflow.shared.model.Document;

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
