package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.responses.SaveDocumentResponse;

public class SaveDocumentRequest extends BaseRequest<SaveDocumentResponse> {

	private OutputDocument doc;
	
	public SaveDocumentRequest() {
	}
	
	public SaveDocumentRequest(OutputDocument doc){
		this.doc = doc;
	}

	public OutputDocument getDoc() {
		return doc;
	}

	public void setDoc(OutputDocument doc) {
		this.doc = doc;
	}
	
}
