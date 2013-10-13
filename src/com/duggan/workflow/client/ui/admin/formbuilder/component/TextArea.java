package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class TextArea extends Field {

	private static TextAreaUiBinder uiBinder = GWT
			.create(TextAreaUiBinder.class);
	
	private final Widget widget;

	interface TextAreaUiBinder extends UiBinder<Widget, TextArea> {
	}

	public TextArea() {
		super();
		widget= uiBinder.createAndBindUi(this);
		
		add(widget);
	}

	@Override
	public Field cloneWidget() {
		return new TextArea();
	}

}
