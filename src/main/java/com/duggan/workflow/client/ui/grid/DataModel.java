package com.duggan.workflow.client.ui.grid;

import java.util.HashMap;

public class DataModel extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	public DataModel(){}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void set(String key, Object value){
		put(key, value);
	}
	
	public boolean isEmpty(){
		if(values().isEmpty()){
			return true;
		}
		for(Object value: values()){
			if(value!=null && !value.toString().isEmpty()){
				return false;
			}
		}
		return true;
	}
}
