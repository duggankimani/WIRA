package com.duggan.workflow.shared.model.form;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;

public class Property extends FormModel{

	private DataType type;
	private Long fieldId;
	private Long formId;
	private Value value;
	private String caption;
	
	public Property(){
	}

	public Property(String name, String caption, DataType type) {
		this.name = name;
		this.caption = caption;
		this.type = type;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	
}
