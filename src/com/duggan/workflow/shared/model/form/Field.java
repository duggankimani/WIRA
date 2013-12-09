package com.duggan.workflow.shared.model.form;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;

public class Field extends FormModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Property> properties = new ArrayList<Property>();
	private List<KeyValuePair> selectionValues = new ArrayList<KeyValuePair>();
	private Value value;
	private DataType type;
	private Long formId=-1L;
	private int position;
	
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
	
	@Override
	public String toString() {
		String str = "Id= "+getId()+";" +
				" FormId = "+getFormId()+";"+
				" Name = "+getName()+";"+
				" Caption = "+getCaption()+";"+
				" Type = "+type+";" +
				" Position = "+position+";";
		
		return str;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<KeyValuePair> getSelectionValues() {
		return selectionValues;
	}

	public void setSelectionValues(List<KeyValuePair> selectionValues) {
		this.selectionValues = selectionValues;
	}
}
