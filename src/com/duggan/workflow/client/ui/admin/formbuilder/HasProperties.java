package com.duggan.workflow.client.ui.admin.formbuilder;

import java.util.List;

import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Property;

public interface HasProperties {

	/*Common Names*/
	public static final String NAME="NAME";
	public static final String CAPTION="CAPTION";
	public static final String HELP="HELP";
	public static final String MANDATORY="MANDATORY";
	public static final String SELECTIONTYPE="SELECTIONTYPE";
	public static final String READONLY="READONLY";
	public static final String PLACEHOLDER="PLACEHOLDER";
	
	public static final String COLUMNLABEL="COLUMNLABEL";
	public static final String COLUMNTYPE="COLUMNTYPE";
	
	public static final String COLUMNPROPERTY="COLUMNPROPERTY";
	
	void addProperty(Property property);
	
	String getPropertyValue(String propertyName);
	
	Object getValue(String propertyName);
	
	Value getFieldValue();
	
	List<Property> getProperties();
}
