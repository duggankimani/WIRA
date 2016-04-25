package com.duggan.workflow.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum ApproverAction implements IsSerializable{

	APPROVED("approved"),
	REJECTED("rejected"),
	RETURNED("returned"),
	DELEGATE("delegated"),
	UPLOADFILE("uploaded"),
	REVIEWED("reviewed"),
	COMPLETED("completed");
	
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
