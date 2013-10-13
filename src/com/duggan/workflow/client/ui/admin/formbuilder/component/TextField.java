package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class TextField extends Field {

	private static TextFieldUiBinder uiBinder = GWT
			.create(TextFieldUiBinder.class);

	interface TextFieldUiBinder extends UiBinder<Widget, TextField> {
	}

	@UiField Element lblEl;
	@UiField com.duggan.workflow.client.ui.component.TextField txtComponent;
		
	@UiField AbsolutePanel container;
	
	private final Widget widget;
	
	public TextField() {
		super();
		properties.add(new Property("PLACEHOLDER", "Place Holder", DataType.STRING));
		properties.add(new Property("PLACEHOLDER", "Place Holder", DataType.STRING));
		properties.add(new Property("PLACEHOLDER", "Place Holder", DataType.STRING));
		widget = uiBinder.createAndBindUi(this);
		//initWidget(uiBinder.createAndBindUi(this));
		add(widget);
	}
	
	public TextField(Property property) {
		this();
		lblEl.setInnerHTML(property.getCaption());
		txtComponent.setName(property.getName());	
		txtComponent.setClass("input-large"); //Smaller TextField
	}

	@Override
	public Field cloneWidget() {
		return new TextField();
	}
	
	
}
