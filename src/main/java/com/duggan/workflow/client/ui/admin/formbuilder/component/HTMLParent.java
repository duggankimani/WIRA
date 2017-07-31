package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.HashMap;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;

public abstract class HTMLParent extends FieldWidget {

	HashMap<String, Field> children = new HashMap<String, Field>();
	ArrayList<FieldWidget> fieldWidgets = new ArrayList<FieldWidget>();

	public HTMLParent() {
		super();
	}

	@Override
	public void setField(Field aField) {
		
		for (Field child : aField.getFields()) {
			child.setForm(aField.getFormId(), aField.getFormRef());
//			child.addValue(new KeyValuePair(READONLY, aField.getProperty(READONLY)));
			children.put(child.getName(), child);
		}
		super.setField(aField);
	}

	public String getElementType(Field fld, Element element) {
		if(fld==null){
			Window.alert("getElementType [1] - Null field");
		}
		if (fld.getType() == DataType.CHECKBOXGROUP) {
			return "checkbox";
		} else if (fld.getType() == DataType.BOOLEAN) {
			return "radio";
		} else if (fld.getType() == DataType.CHECKBOXGROUP) {
			return "checkboxgroup";
		}else if (fld.getType() == DataType.GRID) {
			return "grid";
		}else if(fld.getType() == DataType.SELECTBASIC) {
			return "select";	
		} else if(fld.getType() == DataType.BUTTON){
			return "a";
		}else{
			return element.getAttribute("type");
		}
	}

	public Element getElementById(Element parent, String id) {
		JavaScriptObject el = getNativeElementById(parent, id);
		if (el == null) {
			return null;
		}

		Element element = Element.as(el);
		return element;
	}

	public native JavaScriptObject getNativeElementById(Element parent,
			String id)/*-{
						var el = $wnd.jQuery(parent).find('#'+id).get(0);
						return el;
						}-*/;

	/**
	 * Merge server side Field properties (e.g. Formula or Triggers) with
	 * generated Front End Field
	 * 
	 * @param widget
	 * @return Merged Fields
	 */
	protected Field initializeChild(FieldWidget widget) {
		Field child = widget.getField();
		// Copy General Metadata Fields from the Parent
		child.setForm(field.getFormId(), field.getFormRef());
		child.setParent(field.getId(), field.getRefId());
		// assert field.getRefId() != null; - HTML Grids may not have been
		// saved, yet they have children as well

		child.setDocId(field.getDocId());
		child.setDocRefId(field.getDocRefId());
		child.setHTMLWrappedField(true);
		if (field.getType() == DataType.GRID) {
			child.setGridName(field.getName());
		}

		String name = child.getName();
		if (children.containsKey(name)) {
			Field dbEl = children.get(name);
			copy(child, dbEl);
		}
		
		widget.setField(child);

		//Read Only From The Parent
		widget.setReadOnly(this.readOnly);
		
		// register handlers
		widget.registerHandlers();
		
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

		// feField.setId(dbField.getId());
		feField.setRefId(dbField.getRefId());
		feField.setId(dbField.getId());
		// Name, Caption, my be ignored - Use Front end values
		feField.setDependentFields(dbField.getDependentFields());
		feField.setDynamicParent(dbField.isDynamicParent());
		feField.setFields(dbField.getFields());
		// feField.setLineRefId(dbField.getLineRefId());
		feField.setProps(dbField.getProps());
		feField.setSelectionValues(dbField.getSelectionValues());
		feField.setValue(dbField.getValue());
	}

}
