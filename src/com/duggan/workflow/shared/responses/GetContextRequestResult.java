package com.duggan.workflow.shared.responses;

import java.lang.Boolean;
import java.lang.String;

import com.duggan.workflow.shared.model.HTUser;

public class GetContextRequestResult extends BaseResult {

	private Boolean isValid;
	private HTUser user;

	@SuppressWarnings("unused")
	public GetContextRequestResult() {
		// For serialization only
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public HTUser getUser() {
		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}

}
