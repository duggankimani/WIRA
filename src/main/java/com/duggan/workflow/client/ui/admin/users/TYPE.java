package com.duggan.workflow.client.ui.admin.users;

public enum TYPE{
	GROUP("Group"),
	USER("User"),
	ORG("Org");
	
	private String displayName;

	private TYPE(String displayName) {
		this.displayName = displayName;
	}
	
	public String displayName(){
		return displayName;
	}
}