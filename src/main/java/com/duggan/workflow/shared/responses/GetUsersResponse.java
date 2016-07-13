package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.response.BaseResponse;

public class GetUsersResponse extends BaseResponse {

	private ArrayList<HTUser> users;

	public GetUsersResponse() {
	}

	public GetUsersResponse(ArrayList<HTUser> users) {
		this.users = users;
	}

	public ArrayList<HTUser> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<HTUser> users) {
		this.users = users;
	}
}
