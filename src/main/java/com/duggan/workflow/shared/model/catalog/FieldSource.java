package com.duggan.workflow.shared.model.catalog;

import com.duggan.workflow.shared.model.Listable;

public enum FieldSource implements Listable{
	
	FORM("Form"),
	GRID("Grid");

	private String displayName;

	private FieldSource(String displayName) {
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
