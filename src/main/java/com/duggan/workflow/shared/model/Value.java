package com.duggan.workflow.shared.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import com.google.gwt.user.client.rpc.IsSerializable;

@XmlTransient
public interface Value extends Serializable, IsSerializable{

	void setId(Long Id);
	
	void setKey(String key);
	
	void setValue(Object value);
	
	Object getValue();
	
	String getKey();
	
	Long getId();
	
	DataType getDataType();

	Value clone(boolean clone);

}
