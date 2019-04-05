package com.duggan.workflow.server.dao.model;

public class UserFilter {

	private String searchTerm;
	private String groupId;
	private String orgName;

	public UserFilter() {
	}

	public UserFilter(String searchTerm, String groupId, String orgName) {
		setSearchTerm(searchTerm);
		setGroupId(groupId);
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
