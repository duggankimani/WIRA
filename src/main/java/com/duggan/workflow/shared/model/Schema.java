package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Schema implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private List<Column> columns = new ArrayList<Column>();
	
	public Schema() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
}
