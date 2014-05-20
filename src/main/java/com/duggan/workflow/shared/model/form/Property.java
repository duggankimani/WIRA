package com.duggan.workflow.shared.model.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;

public class Property extends FormModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataType type;
	private Long fieldId;//field property
	private Long formId; //form property
	private Value value;
	private String caption;
	private List<KeyValuePair> kvp= new ArrayList<KeyValuePair>();
	
	public Property(){
	}

	public Property(String name, String caption, DataType type) {
		this.name = name;
		this.caption = caption;
		this.type = type;
	}
	
	public Property(String name, String caption, DataType type,KeyValuePair... pairs) {
		this.name = name;
		this.caption = caption;
		this.type = type;
		
		for(KeyValuePair p: pairs){
			this.kvp.add(p);
		}
	}
	
	public void addSelectionItem(KeyValuePair pair){
		this.kvp.add(pair);
	}
	
	public Property(String name, String caption, DataType type, Long fieldId){
		this(name, caption, type);
		this.fieldId = fieldId;
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
	
	@Override
	public boolean equals(Object obj) {
		Property other = (Property)obj;
		
		return this.name.equals(other.name);
	}
	
	public void setSelectionValues(List<KeyValuePair> pairs){
		if(pairs!=null){
			kvp.clear();
			kvp.addAll(pairs);
		}
	}
	
	public List<KeyValuePair> getSelectionValues(){
		return kvp;
	}
	
	@Override
	public String toString() {
		return "{id="+getId()+
				";Name="+getName()+
				";Value="+(getValue()==null? null: getValue().getValue())+
				"}"
				;
	}
	
	public void sort(){
		Collections.sort(kvp, new Comparator<KeyValuePair>() {
			public int compare(KeyValuePair o1, KeyValuePair o2) {
				String a = o1.getValue();
				String b = o2.getValue();
				
				return a.compareTo(b);
			};
		});
	}
	
	public Property clone(boolean fullClone){
		Property prop = new Property(name, caption, type);
		prop.setSelectionValues(getSelectionValues());
		prop.setType(type);
		if(fullClone){
			prop.setId(Id);
		}
		
		if(value!=null)
			prop.setValue(value.clone(fullClone));
		
		return prop;
	}
	
}
