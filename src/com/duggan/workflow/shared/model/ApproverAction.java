package com.duggan.workflow.shared.model;

public enum ApproverAction {

	APPROVED("approved"),
	REJECTED("rejected"),
	RETURNED("returned"),
	REVIEWED("reviewed");
	
	String action;
	
	private ApproverAction(String action){
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
