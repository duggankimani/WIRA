package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public abstract class HTMLParent extends FieldWidget{


	HashMap<String, Field> children = new HashMap<String, Field>();
	List<FieldWidget> fieldWidgets = new ArrayList<FieldWidget>();
	
	public HTMLParent() {
		super();
	}
	
	@Override
	public void setField(Field aField) {
		super.setField(aField);
		if (aField.getFields() != null) {
			for (Field child : aField.getFields()) {
				child.setForm(aField.getFormId(), aField.getFormRef());
				children.put(child.getName(), child);
			}
		}

	}
	
	protected void bindHTMLWidgets(Element parent) {
		for (Field child : children.values()) {
			if (child.getName() != null) {
				
				Element element = getElementById(parent,child.getName());

				if (element != null){
					wrapElement(element,getElementType(child, element));
				}
			}
		}
	}
	
	public String getElementType(Field fld, Element element) {
		if(fld.getType()==DataType.CHECKBOXGROUP){
			return "checkbox";
		}else if(fld.getType()==DataType.BOOLEAN){
			return "radio";
		}else if(fld.getType()==DataType.GRID){
			return "grid";
		}
		else{
			return element.getAttribute("type");
		}
	}
	
	public Element getElementById(Element parent, String id){
		JavaScriptObject el = getNativeElementById(parent, id);
		if(el==null){
			return null;
		}
		
		Element element = Element.as(el);
		return element;
	}
	
	public native JavaScriptObject getNativeElementById(Element parent, String id)/*-{
		var el = $wnd.jQuery(parent).find('#'+id).get(0);
		return el;
	}-*/;
	
	protected void wrapElement(Element element) {
		wrapElement(element,element.getAttribute("type"));
	}
	
	protected void wrapElement(Element element, String type) {
		FieldWidget widget = FieldWidget.wrap(element,type, designMode);
		
		if (widget != null) {
			Field child = initializeChild(widget);
			children.put(child.getName(), child);
			fieldWidgets.add(widget);
		}
		
		field.setFields(children.values());
	}

	/**
	 * Merge server side Field properties (e.g. Formula or Triggers) with generated Front End Field
	 * 
	 * @param widget
	 * @return Merged Fields
	 */
	protected Field initializeChild(FieldWidget widget) {
		Field child = widget.getField();
		// Copy General Metadata Fields from the Parent
		child.setForm(field.getFormId(),field.getRefId());
		child.setParent(null,field.getRefId());
		//assert field.getRefId() != null; - HTML Grids may not have been saved, yet they have children as well

		child.setDocId(field.getDocId());
		child.setDocRefId(field.getDocRefId());
		child.setHTMLWrappedField(true);

		String name = child.getName();
		if (children.containsKey(name)) {
			Field dbEl = children.get(name);
			copy(child, dbEl);
		}
		widget.setField(child);
		
		return child;
	}

	/**
	 * Copy DB Field values into Front End Field
	 * 
	 * @param feField
	 * @param dbField
	 */
	protected void copy(Field feField, Field dbField) {
		if (dbField == null) {
			return;
		}

//		feField.setId(dbField.getId());
		feField.setRefId(dbField.getRefId());
		// Name, Caption, my be ignored - Use Front end values
		feField.setDependentFields(dbField.getDependentFields());
		feField.setDocRefId(dbField.getDocRefId());
		feField.setDynamicParent(dbField.isDynamicParent());
		feField.setFields(dbField.getFields());
		feField.setForm(dbField.getFormId(),dbField.getRefId());
		feField.setGridName(dbField.getGridName());
		feField.setLineRefId(dbField.getLineRefId());

		feField.setProps(dbField.getProps());

		feField.setSelectionValues(dbField.getSelectionValues());
		feField.setValue(dbField.getValue());
	}

}
