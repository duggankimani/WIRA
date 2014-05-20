package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.Document;

public class SearchDocumentRequestResult extends BaseResponse {

	private List<Document> document;

	public SearchDocumentRequestResult() {
	}

	public SearchDocumentRequestResult(List<Document> document) {
		this.document = document;
	}

	public List<Document> getDocument() {
		return document;
	}

	public void setDocument(List<Document> document) {
		this.document = document;
	}
}
