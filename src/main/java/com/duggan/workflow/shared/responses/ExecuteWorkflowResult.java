package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Doc;

public class ExecuteWorkflowResult extends BaseResponse {

	Doc document;
	
	public ExecuteWorkflowResult() {
	}

	public Doc getDocument() {
		return document;
	}

	public void setDocument(Doc document) {
		this.document = document;
	}
	
}
