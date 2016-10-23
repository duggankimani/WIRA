package com.duggan.workflow.shared.model;

import com.wira.commons.shared.models.SerializableObj;

public class Column extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String caption;
	private String definition;
	private String fieldType;
	private boolean isNull;
	private boolean isPrimaryKey;
	private int length;
	private String width;

	public Column() {
	}

	public Column(String refId, String name, String caption) {
		setRefId(refId);
		this.name = name;
		this.caption = caption;
	}
	
	public Column(String refId, String name, String caption, String width) {
		this(refId,name,caption);
		this.width = width;
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

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	public String getFieldType() {
		return fieldType;
	}

}
