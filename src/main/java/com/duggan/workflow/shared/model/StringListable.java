package com.duggan.workflow.shared.model;

import java.io.Serializable;

import com.wira.commons.shared.models.Listable;

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

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		if(! (obj instanceof StringListable)){
			return false;
		}
		
		return value.equals(((StringListable)obj).value);
	}
	
}
