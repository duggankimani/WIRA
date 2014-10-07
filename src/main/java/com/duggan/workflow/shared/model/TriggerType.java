package com.duggan.workflow.shared.model;

public enum TriggerType {

	BEFORESTEP("Before"),
	AFTERSTEP("After");

	private String display;
	
	private TriggerType(String display){
		this.display = display;
	}
	
	public String display() {
		
		return display;
	}
}
