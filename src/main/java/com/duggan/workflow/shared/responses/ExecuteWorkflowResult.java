package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Doc;
import com.wira.commons.shared.response.BaseResponse;

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
