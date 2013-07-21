package com.duggan.workflow.shared.model;

public enum Priority {

	NORMAL,
	HIGH,
	CRITICAL;

	public static Priority get(Integer priority) {

		
		return Priority.values()[priority==null? 0: priority>2? 2: priority];
	}
}
