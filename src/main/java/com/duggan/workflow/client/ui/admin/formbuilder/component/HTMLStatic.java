package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class HTMLStatic extends FieldWidget{

	Element lblEl;
	
	public HTMLStatic(Element el,boolean isDesignMode) {
		super();
		this.lblEl = el;
		this.designMode = isDesignMode;
		
		if (designMode) {
			showPropertiesOnClick(el);
		}
	}
	
	@Override
	public void defaultProperties() {
		addProperty(new Property(NAME, "Name", DataType.STRING, refId));
		addProperty(new Property(STATICCONTENT, "Content", DataType.STRINGLONG, refId));
		addProperty(new Property(HELP, "Help", DataType.STRING, refId));
	}

	@Override
	public FieldWidget cloneWidget() {
		return new HTMLStatic(lblEl,designMode);
	}

	@Override
	protected DataType getType() {
		return DataType.LABEL;
	}
	
	@Override
	public void setField(Field field) {
		super.setField(field);
		
		Object val  = field.getValue()==null? null: field.getValue().getValue();
		String value = null;
		if(val!=null){
			value = val.toString();
		}else{
			value = getPropertyValue(STATICCONTENT);
		}
		
		if(value!=null){
			lblEl.setInnerHTML(value);
		}
	}
	

	@Override
	public Field getField() {
		setProperty(NAME, lblEl.getId());
		field.setName(lblEl.getId());
		return super.getField();
	}
	
	@Override
	protected void setStaticContent(String content) {
		if(content!=null){
			lblEl.setInnerHTML(content);
		}
	}
	
	@Override
	public void setTitle(String title) {
		if(title!=null)
			lblEl.setTitle(title);
	}
	
	@Override
	public boolean isMandatory() {
		return false;
	}
	
	@Override
	public boolean isReadOnly() {
		return true;
	}
	
	public Widget getComponent() {
		return this;
	}

	@Override
	public Widget getInputComponent() {
		return this;
	}

	@Override
	public Element getViewElement() {
		return null;
	}
	
	@Override
	public void setComponentValid(boolean isValid) {
		
	}

}
