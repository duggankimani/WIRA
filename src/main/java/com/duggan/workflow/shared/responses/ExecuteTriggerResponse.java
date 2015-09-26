package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Doc;

public class ExecuteTriggerResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Doc document;

	public ExecuteTriggerResponse() {
		// For serialization only
	}

	public Doc getDocument() {
		return document;
	}

	public void setDocument(Doc document) {
		this.document = document;
	}
}
