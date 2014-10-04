package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.OutputDocument;

public class SaveDocumentResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputDocument doc;

	public SaveDocumentResponse() {
	}

	public OutputDocument getDoc() {
		return doc;
	}

	public void setDoc(OutputDocument doc) {
		this.doc = doc;
	}

}
