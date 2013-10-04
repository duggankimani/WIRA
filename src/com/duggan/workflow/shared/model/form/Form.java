package com.duggan.workflow.shared.model.form;

import java.util.List;

public class Form extends FormModel{

	private List<Property> properties;
	private List<Field> fields;
	
	public Form() {
	}
	
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	
}
