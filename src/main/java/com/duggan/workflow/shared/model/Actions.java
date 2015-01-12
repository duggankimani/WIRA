package com.duggan.workflow.shared.model;

/**
 * 
 * @author duggan
 * 
 * Human Task Life Cycle
 * 
 */
public enum Actions implements Listable{
	CREATE("Task Creation"),
	CLAIM("Claim"),
	START("Start"),
	SUSPEND("Suspend"),		
	RESUME("Resume"),
	COMPLETE("Task Completion"),
	DELEGATE("Delegate"),
	REVOKE("Revoke"),
	STOP("Stop"),
	FORWARD("Forward");

	private String displayName;

	private Actions(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	
	
}
