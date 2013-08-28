package com.duggan.workflow.shared.responses;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;

import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.UserGroup;

public class GetContextRequestResult extends BaseResponse {

	private Boolean isValid;
	private HTUser user;
	private List<UserGroup> groups;

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

	public List<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}

}
