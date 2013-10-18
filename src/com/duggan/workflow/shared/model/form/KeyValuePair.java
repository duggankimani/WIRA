package com.duggan.workflow.shared.model.form;

import java.io.Serializable;
import java.lang.String;

public class KeyValuePair implements Serializable {

	private static final long serialVersionUID = -5161552354221706835L;
	private String key;
	private String value;

	public KeyValuePair() {
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
}
