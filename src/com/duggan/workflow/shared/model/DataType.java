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
	LAYOUTHR,
	GRID;
	
	public boolean isLookup(){
		return this.equals(SELECTBASIC) || this.equals(SELECTMULTIPLE) || this.equals(BOOLEAN);
	}
}
