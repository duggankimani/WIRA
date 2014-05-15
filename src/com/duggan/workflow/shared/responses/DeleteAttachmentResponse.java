package com.duggan.workflow.shared.responses;


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
