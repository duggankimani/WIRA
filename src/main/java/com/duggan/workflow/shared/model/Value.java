package com.duggan.workflow.shared.model;

import java.io.Serializable;

public interface Value extends Serializable{

	void setId(Long Id);
	
	void setKey(String key);
	
	void setValue(Object value);
	
	Object getValue();
	
	String getKey();
	
	Long getId();
	
	DataType getDataType();

	Value clone(boolean clone);

}
