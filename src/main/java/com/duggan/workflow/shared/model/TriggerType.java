package com.duggan.workflow.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum TriggerType implements IsSerializable {

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
