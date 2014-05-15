package com.duggan.workflow.shared.responses;


public class CheckPasswordRequestResult extends BaseResponse {

	private Boolean isValid;

	public CheckPasswordRequestResult() {
		// For serialization only
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
}
