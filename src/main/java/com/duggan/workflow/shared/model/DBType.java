package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.wira.commons.shared.models.Listable;

public enum DBType implements Listable,Serializable{

	BIGINT,
	BOOLEAN,
	CHAR,
	DATE,
	DATETIME,
	DECIMAL,
	DOUBLE,
	FLOAT,
	INTEGER,
	LONGVARCHAR,
	REAL,
	SMALLINT,
	TIME,
	TINYINT,
	VARCHAR,
	TEXT;

	private DBType() {
	}
	
	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return name();
	}
	
	public static ArrayList<Listable> getValues(){
		ArrayList<Listable> values = new ArrayList<Listable>();
		for(DBType t: values()){
			values.add(t);
		}
		return values;
	}

	public DataType getFieldType() {
		DataType dt = DataType.STRING;
		switch (this) {
		case BIGINT:
		case SMALLINT:
		case TINYINT:
		case INTEGER:
			dt = DataType.INTEGER;
			break;
		case DECIMAL:
		case DOUBLE:
		case FLOAT:		
		case REAL:
			dt = DataType.DOUBLE;
			break;
		case BOOLEAN:
			dt = DataType.BOOLEAN;
			break;
		case CHAR:
		case LONGVARCHAR:
		case VARCHAR:
		case TIME:
			dt = DataType.STRING;
		break;
		case DATE:
		case DATETIME:
			dt = DataType.DATE;
		break;
		}
		return dt;
	}

	public boolean isNumber() {
		
		switch (this) {
		case BIGINT:
		case SMALLINT:
		case TINYINT:
		case INTEGER:
		case DECIMAL:
		case DOUBLE:
		case FLOAT:		
		case REAL:
			return true;
		}
		return false;
	}	
}
