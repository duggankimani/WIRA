package com.duggan.workflow.shared.responses;

import com.wira.commons.shared.response.BaseResponse;


public class DeleteAttachmentResponse extends BaseResponse {

	private Boolean isDeleted;

	public DeleteAttachmentResponse() {
		// For serialization only
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
