package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Doc;
import com.wira.commons.shared.response.BaseResponse;

public class GenerateFilePathResponse extends BaseResponse {

	private Doc doc;

	public GenerateFilePathResponse() {
	}

	public Doc getDoc() {
		return doc;
	}

	public void setDoc(Doc doc) {
		this.doc = doc;
	}

}
