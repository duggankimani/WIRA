package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Document;
import com.wira.commons.shared.response.BaseResponse;

public class SearchDocumentRequestResult extends BaseResponse {

	private ArrayList<Document> document;

	public SearchDocumentRequestResult() {
	}

	public SearchDocumentRequestResult(ArrayList<Document> document) {
		this.document = document;
	}

	public ArrayList<Document> getDocument() {
		return document;
	}

	public void setDocument(ArrayList<Document> document) {
		this.document = document;
	}
}
