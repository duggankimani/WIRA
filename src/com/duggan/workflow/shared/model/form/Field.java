package com.duggan.workflow.shared.model.form;

import java.util.List;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;

public class Field extends FormModel{

	private List<Property> properties;
	private Value value;
	private DataType type;
	private Long formId;
	
	public Field() {
	}
	
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
}
