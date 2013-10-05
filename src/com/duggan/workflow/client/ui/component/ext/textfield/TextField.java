package com.duggan.workflow.client.ui.component.ext.textfield;

import com.duggan.workflow.client.ui.component.ext.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TextField extends Field {

	private static TextFieldUiBinder uiBinder = GWT
			.create(TextFieldUiBinder.class);

	interface TextFieldUiBinder extends UiBinder<Widget, TextField> {
	}

	@UiField AbsolutePanel container;
	
	private final Widget widget;
	
	public TextField() {
		super();
		widget = uiBinder.createAndBindUi(this);
		//initWidget(uiBinder.createAndBindUi(this));
		add(widget);
	}
	
	@Override
	public Field cloneWidget() {
		return new TextField();
	}
	
	
}
