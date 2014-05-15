package com.duggan.workflow.shared.model.form;

import java.io.Serializable;

import com.duggan.workflow.shared.model.Listable;

public class KeyValuePair implements Serializable, Listable {

	private static final long serialVersionUID = -5161552354221706835L;
	private String key;
	private String value;

	public KeyValuePair() {
	}
	
	public KeyValuePair(String key, String value) {
		this.key =key;
		this.value =value;
	}  
	
	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String getName() {
		return key;
	}

	@Override
	public String getDisplayName() {
		return value;
	}
	
	@Override
	public String toString() {
		
		return " [\"Key\"= "+key+",\"Value\"= "+value+"]";
	}
}
