package com.duggan.workflow.shared.responses;

import com.wira.commons.shared.response.BaseResponse;


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
