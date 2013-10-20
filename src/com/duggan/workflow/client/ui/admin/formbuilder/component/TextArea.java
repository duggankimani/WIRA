package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class TextArea extends FieldWidget {

	private static TextAreaUiBinder uiBinder = GWT
			.create(TextAreaUiBinder.class);
	
	private final Widget widget;

	interface TextAreaUiBinder extends UiBinder<Widget, TextArea> {
	}

	@UiField Element lblEl;
	@UiField com.duggan.workflow.client.ui.component.TextArea txtComponent;
	
	public TextArea() {
		super();
		addProperty(new Property(PLACEHOLDER, "Place Holder", DataType.STRING, id));
		widget= uiBinder.createAndBindUi(this);
		txtComponent.getElement().setAttribute("id", "textarea");
		add(widget);
	}

	@Override
	public FieldWidget cloneWidget() {
		return new TextArea();
	}
	
	@Override
	protected void setCaption(String caption) {
		lblEl.setInnerHTML(caption);
	}
	
	@Override
	protected void setPlaceHolder(String placeHolder) {
		txtComponent.setPlaceholder(placeHolder);
	}
	
	@Override
	protected void setHelp(String help) {
		txtComponent.setTitle(help);
	}
	
	@Override
	protected DataType getType() {
		return DataType.STRINGLONG;
	}

	@Override
	public Value getFieldValue() {
		
		String value = txtComponent.getValue();
		
		if(value==null || value.isEmpty())
			return null;
		
		return new StringValue(value);
	}
}
