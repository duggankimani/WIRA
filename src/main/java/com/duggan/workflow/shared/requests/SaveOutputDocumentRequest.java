package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveOutputDocumentResponse;

public class SaveOutputDocumentRequest extends BaseRequest<SaveOutputDocumentResponse> {

	private OutputDocument doc;
	
	public SaveOutputDocumentRequest() {
	}
	
	public SaveOutputDocumentRequest(OutputDocument doc){
		this.doc = doc;
	}

	public OutputDocument getDoc() {
		return doc;
	}

	public void setDoc(OutputDocument doc) {
		this.doc = doc;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveOutputDocumentResponse();
	}
	
}
