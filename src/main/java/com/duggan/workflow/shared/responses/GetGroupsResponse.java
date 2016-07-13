package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.wira.commons.shared.models.UserGroup;
import com.wira.commons.shared.response.BaseResponse;

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
