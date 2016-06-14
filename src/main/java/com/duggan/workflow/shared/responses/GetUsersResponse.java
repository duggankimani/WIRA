package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.HTUser;

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
