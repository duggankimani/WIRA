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
	GRID,
	COLUMNPROPERTY, 
	FILEUPLOAD, FORM, LINK, IFRAME,
	JS;
	
	public boolean isDropdown(){
		return this.equals(SELECTBASIC);
	}
	//GRID;
	
	public boolean isLookup(){
		return this.equals(SELECTBASIC) || this.equals(SELECTMULTIPLE) || this.equals(BOOLEAN);
	}
	
	public DBType toDBType(){
		DBType type = null;
		switch (this) {
		case STRING:
		case STRINGLONG:
		case SELECTBASIC:
		case LINK:
		case LABEL:
			type = DBType.VARCHAR;
			break;
		case INTEGER:
		case FILEUPLOAD:
			type = DBType.INTEGER;
			break;
		case DOUBLE:
			type = DBType.DECIMAL;
			break;
		case DATE:
			type = DBType.DATE;
			break;
		case CHECKBOX:
			type = DBType.BOOLEAN;
			break;
		default:
			type = DBType.VARCHAR;
			break;
		}
		
		return type;
	}
}
