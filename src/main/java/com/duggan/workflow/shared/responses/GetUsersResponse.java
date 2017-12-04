package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.wira.commons.shared.models.HTUser;

public class GetUsersResponse extends BaseListResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<HTUser> users;
	private Integer userCount;

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

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
}
