package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Document;
import java.util.List;

public class SearchDocumentRequestResult extends BaseResult {

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
