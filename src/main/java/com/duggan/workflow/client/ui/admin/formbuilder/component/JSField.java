package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class JSField extends FieldWidget {

	private static JSFieldUiBinder uiBinder = GWT.create(JSFieldUiBinder.class);

	interface JSFieldUiBinder extends UiBinder<Widget, JSField> {
	}

	private final Widget widget;

	@UiField
	Element lblEl;

	public JSField() {
		super();
		widget = uiBinder.createAndBindUi(this);
		add(widget);
	}

	@Override
	public void defaultProperties() {
		addProperty(new Property(NAME, "Name", DataType.STRING, refId));
		addProperty(new Property(JS, "Content", DataType.STRINGLONG,
				refId));
		addProperty(new Property(HELP, "Help", DataType.STRING, refId));
	}

	@Override
	public FieldWidget cloneWidget() {
		return new JSField();
	}

	@Override
	protected DataType getType() {
		return DataType.JS;
	}

	@Override
	public void setField(Field field) {
		super.setField(field);
		String value = getPropertyValue(NAME);
		
		if(value!=null){
			lblEl.setInnerText(value);
		}else{
			lblEl.setInnerText("Javascript");
		}
	}

	@Override
	public void setTitle(String title) {
		if (title != null)
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
