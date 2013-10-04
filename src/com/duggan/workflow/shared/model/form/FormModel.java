package com.duggan.workflow.shared.model.form;

public class FormModel {

	public static final String FORMMODEL = "FORMMODEL";
	public static final String FIELDMODEL = "FIELDMODEL";
	public static final String PROPERTYMODEL = "PROPERTYMODEL";
	
	protected Long Id;
	protected String name;
	protected String caption;
	
	public FormModel() {
		
	}
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
}
