package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.UserGroup;

public class GetGroupsResponse extends BaseResponse{

	private ArrayList<UserGroup> groups;

	public GetGroupsResponse() {
	}

	public GetGroupsResponse(ArrayList<UserGroup> groups) {
		this.groups = groups;
	}

	public ArrayList<UserGroup> getGroups() {
		return groups;
	}
	
	public UserGroup getGroup() {
		return groups.get(0);
	}
	
	public void setGroups(ArrayList<UserGroup> groups) {
		this.groups = groups;
	}
}
