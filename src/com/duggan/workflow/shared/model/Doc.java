package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Doc implements Serializable,Comparable<Doc>{

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;


	public abstract String getSubject();

	public abstract String getDescription();

	public abstract Date getCreated();

	public abstract Date getDocumentDate();
	
	public abstract Integer getPriority();

	public abstract Object getId();
	
	private Map<String, Value> values = new HashMap<String, Value>();
	
	/**
	 * Sorts document/task elements in descending order
	 * hence the negative sign (-)
	 */
	@Override
	public int compareTo(Doc o) {
		
		return - getCreated().compareTo(o.getCreated());
	}

	public Map<String, Value> getValues() {
		return values;
	}

	public void setValues(Map<String, Value> values) {		
		this.values = values;
	}
	
	public void setValue(String name, Value value){
		if(value!=null){
			value.setKey(name);
		}
		values.put(name, value);
	}
	
	public abstract HTUser getOwner();
	
	public abstract Long getProcessInstanceId();

}