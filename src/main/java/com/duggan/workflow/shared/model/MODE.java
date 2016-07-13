package com.duggan.workflow.shared.model;

import com.wira.commons.shared.models.Listable;


public enum MODE implements Listable{

	EDIT,
	CREATE,
	VIEW;

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public String getDisplayName() {
		return this.name();
	}
}
