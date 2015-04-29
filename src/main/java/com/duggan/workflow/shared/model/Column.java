package com.duggan.workflow.shared.model;

import java.io.Serializable;

public class Column implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String definition;
	private boolean isNull;
	private boolean isPrimaryKey;
	private int length;

	public Column() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
