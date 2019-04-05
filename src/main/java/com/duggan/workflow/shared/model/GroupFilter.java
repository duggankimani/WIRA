package com.duggan.workflow.shared.model;

public class GroupFilter extends SearchFilter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupId;
	private String userId;
	
	public GroupFilter(String searchTerm) {
		super(searchTerm, null, null);
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
