package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.HTUser;
import com.gwtplatform.dispatch.shared.Result;
import java.util.List;

public class GetUsersResponse extends BaseResponse {

	private List<HTUser> users;

	public GetUsersResponse() {
	}

	public GetUsersResponse(List<HTUser> users) {
		this.users = users;
	}

	public List<HTUser> getUsers() {
		return users;
	}

	public void setUsers(List<HTUser> users) {
		this.users = users;
	}
}
