package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Doc;

public class GetDocumentResult extends BaseResponse {

	private Doc doc;

	@SuppressWarnings("unused")
	public GetDocumentResult() {
		// For serialization only
	}

	public GetDocumentResult(Doc doc) {
		this.doc = doc;
	}

	public Doc getDoc() {
		return doc;
	}

	public void setDoc(Doc doc) {
		this.doc = doc;
	}
}
