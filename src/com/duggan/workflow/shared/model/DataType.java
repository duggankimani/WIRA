package com.duggan.workflow.shared.model;

public enum DataType {

	STRING,
	STRINGLONG,
	BOOLEAN,
	INTEGER,
	DOUBLE,
	DATE,
	CHECKBOX,
	MULTIBUTTON,
	SELECTBASIC,
	SELECTMULTIPLE,
	BUTTON;
	
	public boolean isDropdown(){
		return this.equals(SELECTBASIC);
	}
}
