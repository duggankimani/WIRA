package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Doc;
import com.wira.commons.shared.response.BaseResponse;

public class ExecuteTriggersResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Doc document;

	public ExecuteTriggersResponse() {
		// For serialization only
	}

	public Doc getDocument() {
		return document;
	}

	public void setDocument(Doc document) {
		this.document = document;
	}
}
