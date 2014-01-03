package com.duggan.workflow.shared.model.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
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
	
	public Form(Long id, String formId, String caption) {
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
	
	public Form clone(){
		Form form = new Form(null, null, null);
		
		if(properties!=null)
		for(Property prop: properties){
			Property clone = prop.clone();
			clone.setValue(null);
			form.addProperty(clone);
		}
		
		if(fields!=null)
		for(Field field: fields){
			form.addField(field.clone());
		}
		
		return form;
	}

	private void addField(Field field) {
		if(fields==null)
			fields = new ArrayList<Field>();
		
		fields.add(field);
	}

	private void addProperty(Property prop) {
		if(properties==null){
			properties = new ArrayList<Property>();
		}
		
		properties.add(prop);
	}
	
	
}
