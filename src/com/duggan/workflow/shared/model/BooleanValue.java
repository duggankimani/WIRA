package com.duggan.workflow.shared.model;

import java.io.Serializable;

public class BooleanValue implements ParamValue, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean value;
	
	public BooleanValue(){		
	}
	
	public BooleanValue(Boolean val){
		setValue(val);
	}

	@Override
	public void setValue(Object value) {
		this.value=(Boolean)value;
	}

	@Override
	public Object getValue() {
		return value;
	}

}
