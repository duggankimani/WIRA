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
	LABEL,
	BUTTON,
	LAYOUTHR;
	
	public boolean isDropdown(){
		return this.equals(SELECTBASIC);
	}
	
	public boolean isLookup(){
		return this.equals(SELECTBASIC) || this.equals(SELECTMULTIPLE);
	}
}
