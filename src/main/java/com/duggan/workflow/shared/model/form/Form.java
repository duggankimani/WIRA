package com.duggan.workflow.shared.model.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.shared.model.Listable;

public class Form extends FormModel implements Listable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Property> properties;
	private List<Field> fields;
	private Long processDefId;
	private String processRefId;
	
	/**
	 * Map<Parent,Children> dependency map
	 */
	private Map<String, List<String>> dependencies = new HashMap<String, List<String>>();
	
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
		return clone(false);
	}
	
	public Form clone(boolean fullClone){
		Form form = new Form(null, null, null);
				
		if(properties!=null)
		for(Property prop: properties){
			Property clone = prop.clone(fullClone);
			clone.setValue(null);
			form.addProperty(clone);
		}
		
		if(fields!=null)
		for(Field field: fields){
			form.addField(field.clone());
		}
		
		return form;
	}

	public void addField(Field field) {
		if(fields==null)
			fields = new ArrayList<Field>();
		
		if(fields.contains(field)){
			fields.remove(field);
		}
		
		fields.add(field);
	}

	private void addProperty(Property prop) {
		if(properties==null){
			properties = new ArrayList<Property>();
		}
		
		properties.add(prop);
	}
	
	@Override
	public String toString() {
		return "[Form Id="+Id
				+",Name="+name
				+",caption="+caption+"]";
	}

	public Long getProcessDefId() {
		return processDefId;
	}

	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}

	public void addFieldDependency(List<String> parentFields, String childField) {
		for(String parentField: parentFields){
			List<String> children = dependencies.get(parentField);
			if(children==null){
				children = new ArrayList<String>();
			}
			children.add(childField);
			dependencies.put(parentField, children);
		}
	}

	public Map<String, List<String>> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Map<String, List<String>> dependencies) {
		this.dependencies = dependencies;
	}

	public String getProcessRefId() {
		return processRefId;
	}

	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}
	
	
}
