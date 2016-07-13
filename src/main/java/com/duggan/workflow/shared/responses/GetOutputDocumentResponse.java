package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.OutputDocument;
import com.wira.commons.shared.response.BaseResponse;

public class GetOutputDocumentResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OutputDocument document = new OutputDocument();
	
	public GetOutputDocumentResponse() {
	}

	public OutputDocument getDocument() {
		return document;
	}

	public void setDocument(OutputDocument document) {
		this.document = document;
	}

}
