package com.duggan.workflow.shared.model;

public enum Priority {

	NORMAL("NORMAL"),
	HIGH("HIGH"),
	CRITICAL("CRITICAL");

	String displayName;
	
	private Priority(String displayName){
		this.displayName = displayName;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	
	public static Priority get(Integer priority) {
		return Priority.values()[priority==null? 0: priority>2? 2: priority];
	}
}
