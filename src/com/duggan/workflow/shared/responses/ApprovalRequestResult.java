package com.duggan.workflow.shared.responses;

import java.lang.Boolean;

public class ApprovalRequestResult extends BaseResponse {

	private Boolean successfulSubmit;

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
}
