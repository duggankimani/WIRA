package com.duggan.workflow.shared.model;

public class LongValue implements ParamValue{

	private Long value;
	
	public LongValue() {
	}
	
	public LongValue(Long value){
		this();
		this.value = value;
	}
	
	@Override
	public void setValue(Object value) {
		this.value = (Long)value;
	}

	@Override
	public Object getValue() {
		
		return value;
	}

}
