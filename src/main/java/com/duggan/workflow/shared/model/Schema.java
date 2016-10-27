package com.duggan.workflow.shared.model;

import java.util.ArrayList;

import com.wira.commons.shared.models.SerializableObj;

public class Schema extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String caption;
	private ArrayList<Column> columns = new ArrayList<Column>();
	
	public Schema() {
	}
	
	public Schema(String refId,String name, String caption) {
		setRefId(refId);
		this.name = name;
		this.caption= caption;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Column> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void addColumn(Column col) {
		columns.add(col);
	}
	
}
