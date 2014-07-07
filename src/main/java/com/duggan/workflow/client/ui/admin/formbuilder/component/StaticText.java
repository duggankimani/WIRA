package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class StaticText extends FieldWidget {

	private static LabelFieldUiBinder uiBinder = GWT
			.create(LabelFieldUiBinder.class);

	private final Widget widget;
	
	interface LabelFieldUiBinder extends UiBinder<Widget, StaticText> {
	}

	@UiField Element lblEl;
	
	public StaticText() {
		super();
		widget = uiBinder.createAndBindUi(this);
		add(widget);
	}
	
	@Override
	public void defaultProperties() {
		addProperty(new Property(NAME, "Name", DataType.STRING, id));
		addProperty(new Property(STATICCONTENT, "Content", DataType.STRINGLONG, id));
		addProperty(new Property(HELP, "Help", DataType.STRING, id));
	}

	@Override
	public FieldWidget cloneWidget() {
		return new StaticText();
	}

	@Override
	protected DataType getType() {
		return DataType.LABEL;
	}
	
	@Override
	public void setField(Field field) {
		super.setField(field);
		String value = getPropertyValue(STATICCONTENT);
		
		if(value!=null){
			lblEl.setInnerHTML(value);
		}else{
			lblEl.setInnerText("Static Text");
		}
	}
	
	@Override
	protected void setStaticContent(String content) {
		lblEl.setInnerHTML(content);
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
}