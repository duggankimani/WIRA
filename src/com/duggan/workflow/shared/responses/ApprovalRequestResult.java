package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Document;

public class ApprovalRequestResult extends BaseResponse {

	private Boolean successfulSubmit;
	private Document document;

	@SuppressWarnings("unused")
	private ApprovalRequestResult() {
		// For serialization only
	}

	public ApprovalRequestResult(Boolean successfulSubmit) {
		this.successfulSubmit = successfulSubmit;
	}

	public Boolean getSuccessfulSubmit() {
		return successfulSubmit;
	}

	public void setSuccessfulSubmit(Boolean successfulSubmit) {
		this.successfulSubmit = successfulSubmit;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
