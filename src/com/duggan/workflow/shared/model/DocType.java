package com.duggan.workflow.shared.model;

public enum DocType {

	INVOICE("Invoice"),
	CONTRACT("Contract"),
	LPO("LPO");
	
	String displayName;
	
	private DocType(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
