package com.duggan.workflow.shared.model.form;

import java.io.Serializable;
import java.util.List;

import com.duggan.workflow.shared.model.Listable;

public class Form extends FormModel implements Listable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Property> properties;
	private List<Field> fields;
	
	public Form() {
	}
	
	public Form(long id, String formId, String caption) {
		setId(id);
		setName(formId);
		setCaption(caption);
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

	@Override
	public String getDisplayName() {
		return getCaption();
	}
	
	
}
