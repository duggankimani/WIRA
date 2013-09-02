package com.duggan.workflow.shared.model;

public class StringValue implements ParamValue{

	String value;
	
	public StringValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value.toString();
	}
}
