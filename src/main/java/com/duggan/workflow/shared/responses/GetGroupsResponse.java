package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.UserGroup;

public class GetGroupsResponse extends BaseResponse{

	private List<UserGroup> groups;

	public GetGroupsResponse() {
	}

	public GetGroupsResponse(List<UserGroup> groups) {
		this.groups = groups;
	}

	public List<UserGroup> getGroups() {
		return groups;
	}
	
	public UserGroup getGroup() {
		return groups.get(0);
	}
	
	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}
}
