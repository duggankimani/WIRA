package com.duggan.workflow.shared.model;

import java.io.Serializable;

public class StringListable implements Listable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String value;

	public StringListable() {
	}
	
	public StringListable(String value){
		this.value = value;
	}
	
	@Override
	public String getName() {
		return value;
	}

	@Override
	public String getDisplayName() {
		return value;
	}

	
}
