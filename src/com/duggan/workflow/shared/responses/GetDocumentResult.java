package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Document;

public class GetDocumentResult extends BaseResult {

	private Document document;

	@SuppressWarnings("unused")
	public GetDocumentResult() {
		// For serialization only
	}

	public GetDocumentResult(Document document) {
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
