package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.DocSummary;

public class ExecuteWorkflowResult extends BaseResult {

	DocSummary document;
	
	public ExecuteWorkflowResult() {
	}

	public DocSummary getDocument() {
		return document;
	}

	public void setDocument(DocSummary document) {
		this.document = document;
	}
	
}
