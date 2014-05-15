package com.duggan.workflow.shared.responses;


public class UpdatePasswordResponse extends BaseResponse {

	private boolean isUpdated;

	public UpdatePasswordResponse() {
		// For serialization only
	}

	public UpdatePasswordResponse(Boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	public Boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
}
