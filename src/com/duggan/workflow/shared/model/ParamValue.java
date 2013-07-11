package com.duggan.workflow.shared.model;

import java.io.Serializable;

public interface ParamValue extends Serializable{

	void setValue(Object value);
	
	Object getValue();
}
